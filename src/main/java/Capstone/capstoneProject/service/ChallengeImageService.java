package Capstone.capstoneProject.service;

import Capstone.capstoneProject.dto.Chats.ChatMessageDTO;
import Capstone.capstoneProject.dto.Chats.ImageUploadRequest;
import Capstone.capstoneProject.entity.Chats.ChatImages;
import Capstone.capstoneProject.entity.Chats.ChatMessages;
import Capstone.capstoneProject.entity.Chats.ChatRoomUsers;
import Capstone.capstoneProject.entity.Chats.ChatRooms;
import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.enums.ChatRoomRole;
import Capstone.capstoneProject.enums.MessageType;
import Capstone.capstoneProject.exceptions.badRequest.TextMessageRequiredException;
import Capstone.capstoneProject.exceptions.forbidden.ChatImageAccessDeniedException;
import Capstone.capstoneProject.exceptions.forbidden.ChatRoomAccessDeniedException;
import Capstone.capstoneProject.exceptions.notfound.ChatRoomMessageNotFoundException;
import Capstone.capstoneProject.exceptions.notfound.ChatRoomNotFoundException;
import Capstone.capstoneProject.exceptions.notfound.UserNotFoundException;
import Capstone.capstoneProject.exceptions.unauthorized.NotAuthenticatedException;
import Capstone.capstoneProject.repository.*;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChallengeImageService {
    private final UserRepository userRepository;
    private final ChatRoomUsersRepository chatRoomUsersRepository;
    private final ChatRoomsRepository chatRoomsRepository;
    private final ChatMessagesRepository chatMessagesRepository;
    private final ChatImagesRepository chatImagesRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final FirebaseStorageService firebaseStorageService;
    private final AuthenticatedUserUtils authenticatedUserUtils;


    public void sendImage(ImageUploadRequest request, SimpMessageHeaderAccessor accessor) {
        Principal principal = resolvePrincipal(accessor);

        String username = principal.getName();

        Users user = userRepository.findByEmailAndDeletedAtIsNull(username)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        ChatRooms chatRoom = chatRoomsRepository.findByRoomId(request.getRoomId())
                .orElseThrow(() -> new ChatRoomNotFoundException("해당 채팅방을 찾을 수 없습니다."));

        ChatRoomUsers chatRoomUsers = chatRoomUsersRepository.findByChatRooms_RoomIdAndUsers(request.getRoomId(), user)
                .orElseThrow(() -> new ChatRoomAccessDeniedException("해당 채팅방의 유저가 아닙니다."));

        // 이미지 여러개여도 메시지 1개로 처리
        ChatMessages message = ChatMessages.builder()
                .users(user)
                .chatRooms(chatRoom)
                .messageType(MessageType.IMAGE)
                .build();

        chatMessagesRepository.save(message);

        // 이미지 업로드 + 저장
        List<String> imageUrls = new ArrayList<>();

        int order = 0;
        for (MultipartFile image : request.getImages()) {
            String url = firebaseStorageService.uploadImage(image, "chat-images");
            imageUrls.add(url);
            ChatImages chatImages = ChatImages.builder()
                    .chatMessages(message)
                    .imageUrl(url)
                    .orderIndex(order++)
                    .build();

            chatImagesRepository.save(chatImages);
        }

        ChatMessageDTO dto = ChatMessageDTO.image(message, imageUrls);

        // 채팅방 전송
        messagingTemplate.convertAndSend(
                "/sub/challenges/chat/room/" + request.getRoomId(),
                dto
        );
    }

    public void deleteImage(Long messageId) {
        Users user = authenticatedUserUtils.getCurrentUser();

        ChatMessages messages = chatMessagesRepository.findByIdAndIsDeletedFalse(messageId)
                .orElseThrow(() -> new ChatRoomMessageNotFoundException("해당 메시지를 찾을 수 없습니다."));

        if (messages.getMessageType() != MessageType.TEXT) {
            throw new TextMessageRequiredException("텍스트 메시지가 아닙니다.");
        }
        ChatRoomUsers chatRoomUsers = chatRoomUsersRepository.findByChatRooms_RoomIdAndUsers(messages.getChatRooms().getRoomId(), user)
                .orElseThrow(() -> new ChatRoomAccessDeniedException("해당 채팅방의 유저가 아닙니다."));

        // 작성자와 방장만 삭제할 수 있음
        boolean isWriter = messages.getUsers().getId().equals(user.getId());
        boolean isHost = chatRoomUsers.getChatRoomRole() == ChatRoomRole.HOST;

        if (!isWriter && !isHost) {
            throw new ChatImageAccessDeniedException("채팅방의 해당 이미지에 관한 권한이 없습니다.");
        }

        // 해당 메시지의 모든 이미지 조회 (이렇게 해야 묶여있는 이미지 한꺼번에 삭제가능)
        List<ChatImages> chatImages = chatImagesRepository.findByChatMessages(messages);

        for (ChatImages images : chatImages) {
            firebaseStorageService.deleteImage(images.getImageUrl());
        }
        chatImagesRepository.deleteAll(chatImages);

        messages.setIsDeleted(true);
        chatMessagesRepository.save(messages);

        ChatMessageDTO dto = ChatMessageDTO.deleted(messages);

        messagingTemplate.convertAndSend(
                "/sub/challenges/chat/room/" + messages.getChatRooms().getRoomId(),
                dto
        );
    }

    private Principal resolvePrincipal(SimpMessageHeaderAccessor accessor) {

        Principal principal = accessor.getUser();
        if (principal != null) {
            return principal;
        }

        Object saved = accessor.getSessionAttributes().get("userPrincipal");
        if (saved instanceof Principal p) {
            return p;
        }

        throw new NotAuthenticatedException("WebSocket 인증 정보가 없습니다.");
    }
}

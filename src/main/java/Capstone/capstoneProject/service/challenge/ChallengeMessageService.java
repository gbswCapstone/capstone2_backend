package Capstone.capstoneProject.service.challenge;

import Capstone.capstoneProject.dto.chat.*;
import Capstone.capstoneProject.entity.chat.ChatMessages;
import Capstone.capstoneProject.entity.chat.ChatRoomUsers;
import Capstone.capstoneProject.entity.chat.ChatRooms;
import Capstone.capstoneProject.entity.mission.Missions;
import Capstone.capstoneProject.entity.usage.UsageHistory;
import Capstone.capstoneProject.entity.user.Users;
import Capstone.capstoneProject.enums.ChatRoomRole;
import Capstone.capstoneProject.enums.MessageType;
import Capstone.capstoneProject.exceptions.forbidden.ChatMessageAccessDeniedException;
import Capstone.capstoneProject.exceptions.forbidden.ChatRoomAccessDeniedException;
import Capstone.capstoneProject.exceptions.notfound.ChatRoomMessageNotFoundException;
import Capstone.capstoneProject.exceptions.notfound.ChatRoomNotFoundException;
import Capstone.capstoneProject.exceptions.notfound.UserNotFoundException;
import Capstone.capstoneProject.exceptions.unauthorized.NotAuthenticatedException;
import Capstone.capstoneProject.repository.*;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChallengeMessageService {
    private final AuthenticatedUserUtils authenticatedUserUtils;
    private final ChatRoomUsersRepository chatRoomUsersRepository;
    private final UserRepository userRepository;
    private final ChatRoomsRepository chatRoomsRepository;
    private final ChatMessagesRepository chatMessagesRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final UsageHistoryRepository usageHistoryRepository;
    private final ChatImagesRepository chatImagesRepository;
    private final MissionRepository missionRepository;
    private final UserMissionRepository userMissionRepository;

    private String extractRoomId(String destination) {
        return destination.split("/room/")[1];
    }

    public void sendMessage(ChallengeMessageRequest request, SimpMessageHeaderAccessor accessor) {
        Principal principal = resolvePrincipal(accessor);
        String username = principal.getName();
        Users user = userRepository.findByEmailAndDeletedAtIsNull(username)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        String roomId = request.getRoomId();

        // 채팅방 조회
        ChatRooms chatRoom = chatRoomsRepository.findByRoomId(roomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("해당 채팅방을 찾을 수 없습니다."));

        ChatMessages chatMessages = ChatMessages.builder()
                .users(user)
                .chatRooms(chatRoom)
                .messageType(MessageType.TEXT)
                .message(request.getMessage())
                .build();
        chatMessagesRepository.save(chatMessages);

        ChatMessageDTO dto = ChatMessageDTO.text(chatMessages);

        // 메시지 전송
        messagingTemplate.convertAndSend(
                "/sub/challenges/chat/room/" + roomId,
                dto
        );
    }

    public void patchMessage(MessagePatchRequest request, Long messageId) {
        Users user = authenticatedUserUtils.getCurrentUser();

        ChatMessages chatMessages = chatMessagesRepository.findById(messageId)
                .orElseThrow(() -> new ChatRoomMessageNotFoundException("채팅방의 해당 메시지를 찾을 수 없습니다."));


        // 작성자만 수정 가능
        if (!chatMessages.getUsers().getId().equals(user.getId())) {
            throw new ChatMessageAccessDeniedException("채팅방의 해당 메시지에 관한 권한이 없습니다.");
        }

        if (!StringUtils.hasText(request.getMessage())) {
            throw new IllegalArgumentException("메시지 내용은 비어 있을 수 없습니다.");
        }
        chatMessages.setMessage(request.getMessage());
        chatMessagesRepository.save(chatMessages);

        // 수정한거 채팅방에 바로 반영
        ChatMessageDTO dto = ChatMessageDTO.text(chatMessages);
        messagingTemplate.convertAndSend(
                "/sub/challenges/chat/room/" + chatMessages.getChatRooms().getRoomId(),
                dto
        );
    }

    public List<ChatMessageDTO> getMessages(String roomId, int page, int size) {
        Users user = authenticatedUserUtils.getCurrentUser();

        ChatRooms chatRooms = chatRoomsRepository.findByRoomId(roomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("해당 채팅방을 찾을 수 없습니다."));

        ChatRoomUsers chatRoomUser = chatRoomUsersRepository
                .findByChatRoomsAndUsers(chatRooms, user)
                .orElseThrow(() -> new ChatRoomAccessDeniedException("해당 채팅방에 참여하지 않은 유저입니다."));

        LocalDateTime joinedAt = chatRoomUser.getCreatedAt();

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<ChatMessages> messagePage = chatMessagesRepository.findByChatRoomsAndCreatedAtGreaterThanEqual(
                chatRooms, joinedAt, pageable
        );

        Missions commonMission = missionRepository.findByChallenges(chatRooms.getChallenge())
                .stream().findFirst().orElse(null);


        List<ChatMessageDTO> messages = messagePage.getContent().stream()
                .map(message -> {
                    UsageHistory usage = null;
                    List<String> imageUrls = null;
                    int currentParticipants = 0;

                    if (message.getMessageType() == MessageType.USAGE_SHARE) {
                        usage = usageHistoryRepository.findById(message.getUsageId()).orElse(null);
                    }

                    if (message.getMessageType() == MessageType.IMAGE) {
                        imageUrls = chatImagesRepository.findImageUrlByChatMessages(message);
                    }

                    if (message.getMessageType() == MessageType.MISSION) {
                        // 메시지에 직접 연결된 미션이 있다면 사용, 없다면 위에서 조회한 commonMission 사용
                        Missions m = (message.getMissions() != null) ? message.getMissions() : commonMission;
                        if (m != null) {
                            currentParticipants = userMissionRepository.countByMissionsId(m.getId());
                        }
                        return ChatMessageDTO.from(message, usage, imageUrls, m, currentParticipants);
                    }

                    return ChatMessageDTO.from(message, usage, imageUrls, null, 0);
                })
                .collect(Collectors.toList());

        // 과거 -> 최신순
        Collections.reverse(messages);

        return messages;
    }

    public void deleteMessage(Long messageId) {
        Users user = authenticatedUserUtils.getCurrentUser();

        ChatMessages chatMessages = chatMessagesRepository.findByIdAndIsDeletedFalse(messageId)
                .orElseThrow(() -> new ChatRoomMessageNotFoundException("채팅방의 해당 메시지를 찾을 수 없습니다."));

        ChatRoomUsers chatRoomUsers = chatRoomUsersRepository.findByChatRooms_RoomIdAndUsers(chatMessages.getChatRooms().getRoomId(), user)
                .orElseThrow(() -> new ChatRoomAccessDeniedException("해당 채팅방의 유저가 아닙니다."));

        // 작성자랑 방장만 메시지 삭제 가능
        boolean isWriter = chatMessages.getUsers().getId().equals(user.getId());
        boolean isHost = chatRoomUsers.getChatRoomRole() == ChatRoomRole.HOST;

        if (!isWriter && !isHost) {
            throw new ChatMessageAccessDeniedException("채팅방의 해당 메시지에 관한 권한이 없습니다.");
        }
        chatMessages.setIsDeleted(true);
        chatMessagesRepository.save(chatMessages);

        ChatMessageDTO dto = ChatMessageDTO.deleted(chatMessages);

        // 삭제한거 바로 반영
        messagingTemplate.convertAndSend(
                "/sub/challenges/chat/room/" + chatMessages.getChatRooms().getRoomId(),
                dto
        );
    }

    public List<ChatMessageSearchResponse> searchChallengeChatMessage(String roomId, String keyword) {
        Users user = authenticatedUserUtils.getCurrentUser();

        chatRoomUsersRepository.findByChatRooms_RoomIdAndUsers(roomId, user)
                .orElseThrow(() -> new ChatRoomAccessDeniedException("해당 채팅방의 유저가 아닙니다."));

        // 메시지 검색 (텍스트 메시지만)
        List<ChatMessages> messages =
                chatMessagesRepository.searchByRoomIdAndKeyword(roomId, keyword);

        return messages.stream()
                .map(ChatMessageSearchResponse::from)
                .toList();
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

    public void entreeSendMessage(String roomId, Users user) {

        ChatRooms chatRoom = chatRoomsRepository.findByRoomId(roomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("해당 채팅방이 없습니다."));

        // 입장 메시지 전송
        ChatMessages enterEntity = ChatMessages.builder()
                .chatRooms(chatRoom)
                .users(user)
                .messageType(MessageType.ENTER)
                .message(user.getProfile().getNickname() + "님이 입장했습니다.")
                .build();

        enterEntity = chatMessagesRepository.save(enterEntity);


        ChatMessageDTO enterMessage = ChatMessageDTO.baseFrom(enterEntity);


        messagingTemplate.convertAndSend("/sub/challenges/chat/room/" + roomId, enterMessage);
    }

    public void exitSendMessage(String roomId, Users user) {
        ChatRooms chatRoom = chatRoomsRepository.findByRoomId(roomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("해당 채팅방을 찾을 수 없습니다."));

        ChatMessages leaveEntity = ChatMessages.builder()
                .chatRooms(chatRoom)
                .users(user)
                .messageType(MessageType.LEAVE)
                .message(user.getProfile().getNickname() + "님이 퇴장했습니다.")
                .build();

        leaveEntity = chatMessagesRepository.save(leaveEntity);

        ChatMessageDTO leaveMessage = ChatMessageDTO.baseFrom(leaveEntity);

        messagingTemplate.convertAndSend("/sub/challenges/chat/room/" + roomId, leaveMessage);
    }

    public void saveAndSendUsageShareMessage(String roomId, UsageHistory usageHistory, Users user) {

        ChatRooms chatRoom = chatRoomsRepository.findByRoomId(roomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("해당 채팅방이 없습니다."));

        ChatMessages chatMessages = ChatMessages.builder()
                .users(user)
                .chatRooms(chatRoom)
                .messageType(MessageType.USAGE_SHARE)
                .usageId(usageHistory.getId())
                .build();
        chatMessagesRepository.save(chatMessages);

        ChatMessageDTO dto = ChatMessageDTO.usageShare(chatMessages, usageHistory);

        // 메시지 전송
        messagingTemplate.convertAndSend(
                "/sub/challenges/chat/room/" + roomId,
                dto
        );
    }

    public void saveAndSendMissionMessage(String roomId, Missions missions, Users user) {

        ChatRooms chatRoom = chatRoomsRepository.findByRoomId(roomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("해당 채팅방이 없습니다."));

        ChatMessages chatMessages = ChatMessages.builder()
                .users(user)
                .chatRooms(chatRoom)
                .messageType(MessageType.MISSION)
                .missions(missions)
                .build();
        chatMessagesRepository.save(chatMessages);

        int currentParticipants = userMissionRepository.countByMissionsId(missions.getId());
        ChatMessageDTO dto = ChatMessageDTO.missionShare(chatMessages, missions, currentParticipants);

        // 메시지 전송
        messagingTemplate.convertAndSend(
                "/sub/challenges/chat/room/" + roomId,
                dto
        );
    }

    public void updateMissionMessage(String roomId, ChatMessageDTO dto) {
        // 수정한거 채팅방에 바로 반영
        messagingTemplate.convertAndSend(
                "/sub/challenges/chat/room/" + roomId,
                dto
        );
    }

}

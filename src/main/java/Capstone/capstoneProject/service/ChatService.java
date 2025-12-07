package Capstone.capstoneProject.service;

import Capstone.capstoneProject.dto.Chats.ChatMessageDTO;
import Capstone.capstoneProject.entity.Chats.ChatMessages;
import Capstone.capstoneProject.entity.Chats.ChatRoomUsers;
import Capstone.capstoneProject.entity.Chats.ChatRooms;
import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.entity.challenges.Challenges;
import Capstone.capstoneProject.exceptions.ChatRoomNotFoundException;
import Capstone.capstoneProject.repository.ChatMessagesRepository;
import Capstone.capstoneProject.repository.ChatRoomUsersRepository;
import Capstone.capstoneProject.repository.ChatRoomsRepository;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatService {
    private final ChatRoomsRepository chatRoomsRepository;
    private final ChatRoomUsersRepository chatRoomUsersRepository;
    private final ChatMessagesRepository chatMessagesRepository;
    private final AuthenticatedUserUtils authenticatedUserUtils;


    public ChatRooms createRoom(Challenges challenges) {
        ChatRooms chatRooms = ChatRooms.builder()
                .challenge(challenges)
                .roomId(UUID.randomUUID().toString()) // 랜덤 UUID
                .name(challenges.getTitle())
                .build();
        return chatRoomsRepository.save(chatRooms);
    }

    public void enterChatRoom(Challenges challenges, Users user) {
        ChatRooms chatRooms = chatRoomsRepository.findByChallenge(challenges)
                .orElseThrow(() -> new ChatRoomNotFoundException("해당 채팅방을 찾을 수 없습니다."));

//         채팅방 참여자 등록
        chatRoomUsersRepository.findByChatRoomsAndUsers(chatRooms, user)
                .orElseGet(() -> {
                    ChatRoomUsers chatRoomUsers = ChatRoomUsers.builder()
                            .chatRooms(chatRooms)
                            .users(user)
                            .build();
                    return chatRoomUsersRepository.save(chatRoomUsers);
                });
    }

    public List<ChatMessageDTO> getMessages(String roomId, int page, int size) {
        Users user = authenticatedUserUtils.getCurrentUser();
        ChatRooms chatRooms = chatRoomsRepository.findByRoomId(roomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("해당 채팅방을 찾을 수 없습니다."));

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<ChatMessages> messagePage =
                chatMessagesRepository.findByChatRooms(chatRooms, pageable);

        return messagePage.getContent()
                .stream()
                .map(ChatMessageDTO::from)
                .collect(Collectors.toList());

    }



}

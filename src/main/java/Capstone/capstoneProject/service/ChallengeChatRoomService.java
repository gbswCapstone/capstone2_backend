package Capstone.capstoneProject.service;

import Capstone.capstoneProject.dto.Chats.ChatRoomUserResponse;
import Capstone.capstoneProject.entity.Chats.ChatRoomUsers;
import Capstone.capstoneProject.entity.Chats.ChatRooms;
import Capstone.capstoneProject.entity.Users.Users;
import Capstone.capstoneProject.entity.challenges.Challenges;
import Capstone.capstoneProject.enums.ChatRoomRole;
import Capstone.capstoneProject.exceptions.forbidden.ChatRoomAccessDeniedException;
import Capstone.capstoneProject.exceptions.notfound.ChatRoomNotFoundException;
import Capstone.capstoneProject.repository.ChatRoomUsersRepository;
import Capstone.capstoneProject.repository.ChatRoomsRepository;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ChallengeChatRoomService {
    private final AuthenticatedUserUtils authenticatedUserUtils;
    private final ChatRoomUsersRepository chatRoomUsersRepository;
    private final ChatRoomsRepository chatRoomsRepository;

    public ChatRooms createRoom(Challenges challenges) {
        ChatRooms chatRooms = ChatRooms.builder()
                .challenge(challenges)
                .roomId(UUID.randomUUID().toString()) // 랜덤 UUID
                .name(challenges.getTitle())
                .build();
        return chatRoomsRepository.save(chatRooms);
    }

    public List<ChatRoomUserResponse> getChallengeChatUser(String roomId) {
        Users user = authenticatedUserUtils.getCurrentUser();

        chatRoomUsersRepository.findByChatRooms_RoomIdAndUsers(roomId, user)
                .orElseThrow(() -> new ChatRoomAccessDeniedException("해당 채팅방의 유저가 아닙니다."));

        return chatRoomUsersRepository.findByChatRooms_RoomIdOrderByCreatedAtAsc(roomId)
                .stream()
                .map(cru -> ChatRoomUserResponse.from(cru, user))
                .sorted(Comparator
                        .comparing((ChatRoomUserResponse r) -> !r.isHost()) // 방장 먼저 정렬
                        .thenComparing(r -> !r.isMine())) // 그 다음 나
                .toList();
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
                            .chatRoomRole(ChatRoomRole.MEMBER) // 멤버로 추가
                            .build();
                    return chatRoomUsersRepository.save(chatRoomUsers);
                });
    }



}

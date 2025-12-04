package Capstone.capstoneProject.service;

import Capstone.capstoneProject.dto.Chats.ChatRoomEnterResponse;
import Capstone.capstoneProject.entity.Chats.ChatRoomUsers;
import Capstone.capstoneProject.entity.Chats.ChatRooms;
import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.entity.challenges.Challenges;
import Capstone.capstoneProject.exceptions.ChallengeAccessDeniedException;
import Capstone.capstoneProject.exceptions.ChallengeFullException;
import Capstone.capstoneProject.exceptions.ChallengeNotFoundException;
import Capstone.capstoneProject.exceptions.ChatRoomNotFoundException;
import Capstone.capstoneProject.repository.ChallengeRepository;
import Capstone.capstoneProject.repository.ChatRoomUsersRepository;
import Capstone.capstoneProject.repository.ChatRoomsRepository;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ChatService {
    private final AuthenticatedUserUtils authenticatedUserUtils;
    private final ChatRoomsRepository chatRoomsRepository;
    private final ChallengeRepository challengeRepository;
    private final ChatRoomUsersRepository chatRoomUsersRepository;

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

//    public ChatRoomEnterResponse enterChatRoom(Long challengeId) {
//        Users user = authenticatedUserUtils.getCurrentUser();
//
//        Challenges challenges = challengeRepository.findByIdAndDeletedAtIsNull(challengeId)
//                .orElseThrow(() -> new ChallengeNotFoundException("챌린지방을 찾을 수 없습니다."));
//
//        // 챌린지 참여자인지 확인
//        boolean isParticipant = challenges.getChallengeUsers().stream()
//                .anyMatch(cu -> cu.getUser().getId().equals(user.getId()));
//        if (!isParticipant) {
//            throw new ChallengeAccessDeniedException("챌린지방 참가자가 아닙니다.");
//        }
//
//        // 채팅방 조회
//        ChatRooms chatRooms = chatRoomsRepository.findByChallenge(challenges)
//                .orElseThrow(() -> new ChatRoomNotFoundException("해당 채팅방을 찾을 수 없습니다."));
//
//        // 채팅방 참여자 등록
//        chatRoomUsersRepository.findByChatRoomsAndUsers(chatRooms, user)
//                .orElseGet(() -> {
//                    ChatRoomUsers chatRoomUsers = ChatRoomUsers.builder()
//                            .chatRooms(chatRooms)
//                            .users(user)
//                            .build();
//                    return chatRoomUsersRepository.save(chatRoomUsers);
//                });
//
//        // roomId 반환 (클라이언트는 이걸로 webSocket 구독)
//        return ResponseEntity.ok(new ChatRoomEnterResponse(chatRooms.getRoomId()));
//    }

}

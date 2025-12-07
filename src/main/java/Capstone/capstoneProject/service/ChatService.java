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



}

package Capstone.capstoneProject.service;

import Capstone.capstoneProject.entity.Chats.ChatRooms;
import Capstone.capstoneProject.entity.challenges.Challenges;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ChatService {

    public ChatRooms createRoom(Challenges challenges) {
        return ChatRooms.builder()
                .challenge(challenges)
                .roomId(UUID.randomUUID().toString()) // 랜덤 UUID
                .name(challenges.getTitle())
                .build();
    }

}

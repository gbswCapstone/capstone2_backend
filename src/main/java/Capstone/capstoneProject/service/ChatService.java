package Capstone.capstoneProject.service;

import Capstone.capstoneProject.dto.Chats.ChatRoom;
import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.entity.challenges.Challenges;
import Capstone.capstoneProject.exceptions.ChallengeNotFoundException;
import Capstone.capstoneProject.repository.ChallengeRepository;
import Capstone.capstoneProject.repository.ChallengeUsersRepository;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
    private final AuthenticatedUserUtils authenticatedUserUtils;
    private final ChallengeRepository challengeRepository;
    private final ChallengeUsersRepository challengeUsersRepository;
    private final ObjectMapper objectMapper;
    private Map<String, ChatRoom> chatRooms;

    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    public List<ChatRoom> findAllRoom() {
        return new ArrayList<>(chatRooms.values());
    }



    public ChatRoom findRoomById(String roomId) {
        return chatRooms.get(roomId);
    }

    // 채팅방 생성
    public ChatRoom createRoom(String name) {
        String randomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(randomId)
                .name(name)
                .build();
        chatRooms.put(randomId, chatRoom);
        return chatRoom;
    }

    // 그룹 채팅방 들어가기 여기서 Long id는 챌린지 id
    public ChatRoom enterChallengeChatRoom(Long id) {
        Optional<Challenges> challenges = Optional.ofNullable(challengeRepository.findById(id)
                .orElseThrow(() -> new ChallengeNotFoundException("해당 챌린지를 찾을 수 없습니다.")));
        // user 정보 가져오기 (baarer token에서 추출)
        Users user = authenticatedUserUtils.getCurrentUser();
        // 참여 여부 확인
        boolean isAlreadyJoined = challengeUsersRepository.existsByChallengeIdAndUserId(id, user.getId());
        if (!isAlreadyJoined) {
            throw new IllegalArgumentException("챌린지에 가입하지 않은 사용자는 채팅방에 입장할 수 없습니다.");
        }

        String roomId = challenges.get().getRoomId();

        // 채팅방 없으면 생성
        ChatRoom chatRoom = chatRooms.get(roomId);
        if (chatRoom == null) {
            chatRoom = ChatRoom.builder()
                    .roomId(roomId)
                    .name(challenges.get().getTitle() + " 채팅방")
                    .build();
            chatRooms.put(roomId, chatRoom);
        }

        return chatRoom;
    }



}

package Capstone.capstoneProject.dto.Chats;

import Capstone.capstoneProject.enums.MessageType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    private String roomId; // 챌린지방 id
    private String sender; // 메시지 보낸 사람
    private String message; // 메시지
    private MessageType type; // 메시지 타입
}

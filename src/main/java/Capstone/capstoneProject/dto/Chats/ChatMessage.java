package Capstone.capstoneProject.dto;

import Capstone.capstoneProject.enums.MessageType;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
public class ChatMessage {
    private String roomId; // 챌린지방 id
    private String sender; // 메시지 보낸 사람
    private String message; // 메시지
    private MessageType type; // 메시지 타입
}

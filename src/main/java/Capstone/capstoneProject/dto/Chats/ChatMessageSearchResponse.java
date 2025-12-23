package Capstone.capstoneProject.dto.Chats;

import Capstone.capstoneProject.entity.Chats.ChatMessages;
import Capstone.capstoneProject.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ChatMessageSearchResponse {
    private Long messageId;
    private String message;
    private MessageType messageType;
    private Long senderId;
    private String senderName;
    private LocalDateTime createdAt;
    public static ChatMessageSearchResponse from(ChatMessages chatMessages) {
        return ChatMessageSearchResponse.builder()
                .messageId(chatMessages.getId())
                .message(chatMessages.getMessage())
                .messageType(chatMessages.getMessageType())
                .senderId(chatMessages.getUsers().getId())
                .senderName(chatMessages.getUsers().getProfile().getNickname())
                .createdAt(chatMessages.getCreatedAt())
                .build();
    }
}

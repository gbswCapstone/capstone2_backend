package Capstone.capstoneProject.dto.chatbot;

import Capstone.capstoneProject.entity.chatbot.ChatBotMessages;
import Capstone.capstoneProject.enums.ChatBotSenderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ChatBotMessageDTO {
    private Long id; // 메시지 아이디
    private String message;
    private ChatBotSenderType senderType;
    private LocalDateTime createdAt;


    public static ChatBotMessageDTO from(ChatBotMessages chatBotMessages) {
        return ChatBotMessageDTO.builder()
                .id(chatBotMessages.getId())
                .message(chatBotMessages.getMessage())
                .senderType(chatBotMessages.getSenderType())
                .createdAt(chatBotMessages.getCreatedAt())
                .build();
    }
}

package Capstone.capstoneProject.dto.ChatBot;

import Capstone.capstoneProject.entity.ChatBot.ChatBotMessages;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class RecentChats {
    private String sender;
    private String message;

    public static RecentChats from(ChatBotMessages chatBotMessages) {
        return RecentChats.builder()
                .sender(chatBotMessages.getSenderType().name())
                .message(chatBotMessages.getMessage())
                .build();
    }
}

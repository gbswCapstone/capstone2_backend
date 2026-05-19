package Capstone.capstoneProject.dto.chatbot;

import Capstone.capstoneProject.entity.chatbot.HomeChatBotMessages;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ChatBotMessageResponse {
    private String message;

    public static ChatBotMessageResponse homeChatBot(HomeChatBotMessages homeChatBotMessages) {
        return ChatBotMessageResponse.builder()
                .message(homeChatBotMessages.getMessage())
                .build();
    }
}

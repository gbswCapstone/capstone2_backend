package Capstone.capstoneProject.dto.ChatBot;

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
}

package Capstone.capstoneProject.dto.chatbot;

import Capstone.capstoneProject.dto.usage.RecentTransactionDTO;
import Capstone.capstoneProject.entity.chatbot.ChatBotMessages;
import Capstone.capstoneProject.entity.chatbot.HomeChatBotMessages;
import Capstone.capstoneProject.entity.UsageHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ChatBotRoomMessageRequest {
    private String userMessage;
    private List<RecentChats> recentChatsList;
    private List<RecentTransactionDTO> recentTransactions;
    public static ChatBotRoomMessageRequest from(String userMessage, List<ChatBotMessages> chatBotMessages, List<UsageHistory> histories) {
        return ChatBotRoomMessageRequest.builder()
                .userMessage(userMessage)
                .recentChatsList(
                        chatBotMessages.stream().map(RecentChats::from).toList())
                .recentTransactions(histories.stream().map(RecentTransactionDTO::from).toList())
                .build();
    }
}

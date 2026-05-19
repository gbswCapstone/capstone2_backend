package Capstone.capstoneProject.dto.chatbot;

import Capstone.capstoneProject.dto.usage.RecentTransactionDTO;
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
public class ChatBotSummaryRequest {
    private List<ChatBotMessageResponse> recentChats;
    private List<RecentTransactionDTO> recentTransactions;


    public static ChatBotSummaryRequest from(List<HomeChatBotMessages> homeChatBotMessages, List<UsageHistory> histories) {
        return ChatBotSummaryRequest.builder()
                .recentChats(
                        homeChatBotMessages.stream()
                                .map(ChatBotMessageResponse::homeChatBot)
                                .toList()
                )
                .recentTransactions(
                        histories.stream()
                                .map(RecentTransactionDTO::from)
                                .toList()
                )
                .build();
    }
}

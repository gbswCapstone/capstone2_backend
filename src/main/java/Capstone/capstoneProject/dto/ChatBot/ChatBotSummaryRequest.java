package Capstone.capstoneProject.dto.ChatBot;

import Capstone.capstoneProject.dto.Usages.RecentTransactionDTO;
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
    private List<RecentTransactionDTO> recentTransactions;


    public static ChatBotSummaryRequest from(List<UsageHistory> histories) {
        return ChatBotSummaryRequest.builder()
                .recentTransactions(
                        histories.stream()
                                .map(RecentTransactionDTO::from)
                                .toList()
                )
                .build();
    }
}

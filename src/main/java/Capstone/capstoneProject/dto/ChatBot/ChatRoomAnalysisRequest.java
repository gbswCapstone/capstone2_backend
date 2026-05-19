package Capstone.capstoneProject.dto.chatbot;

import Capstone.capstoneProject.dto.usage.UsageItemDTO;
import Capstone.capstoneProject.dto.usage.UsageSummaryDTO;
import Capstone.capstoneProject.entity.chatbot.ChatBotMessages;
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
public class ChatRoomAnalysisRequest {
    private String userMessage;
    private List<RecentChats> recentChats;
    private UsageSummaryDTO usageSummary;
    private List<UsageItemDTO> usageDetail;


    public static ChatRoomAnalysisRequest from(
            String userMessage,
            List<ChatBotMessages> chatBotMessages,
            UsageSummaryDTO usageSummary,
            List<UsageHistory> usageHistories
    ) {
        return ChatRoomAnalysisRequest.builder()
                .userMessage(userMessage)
                .recentChats(
                        chatBotMessages == null
                                ? List.of()
                                : chatBotMessages.stream()
                                .map(RecentChats::from)
                                .toList()
                )
                .usageSummary(usageSummary)
                .usageDetail(
                        usageHistories.stream()
                                .map(UsageItemDTO::from)
                                .toList()
                )
                .build();
    }
}

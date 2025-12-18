package Capstone.capstoneProject.dto.ChatBot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ChatRoomAnalysisResponse {
    private String mainMessage;
    private List<String> insights;
    private List<String> advice;
    private ChatRoomAnalysisSummary summary;

    public ChatRoomAnalysisResponse(ChatRoomAnalysisResponse response) {
        this.mainMessage = response.getMainMessage();
        this.insights = response.getInsights();
        this.advice = response.getAdvice();
        this.summary = response.getSummary();
    }
}

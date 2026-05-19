package Capstone.capstoneProject.dto.chatbot;

import Capstone.capstoneProject.dto.usage.SpendingItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ChatRoomAnalysisSummary {
    private String biggestCategory;
    private SpendingItem mostAmountItemName;
    private String highestPriceItemName;
    private BigDecimal totalOutlay;



}

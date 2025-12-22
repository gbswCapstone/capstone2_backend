package Capstone.capstoneProject.dto.ChatBot;

import Capstone.capstoneProject.dto.Usages.SpendingItem;
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

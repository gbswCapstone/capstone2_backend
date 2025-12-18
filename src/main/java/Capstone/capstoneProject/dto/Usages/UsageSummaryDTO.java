package Capstone.capstoneProject.dto.Usages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UsageSummaryDTO {
    private String month;
    private BigDecimal totalOutlay;
    private BigDecimal totalIncome;
    private List<CategorySummariesDTO> categorySummaries;


}

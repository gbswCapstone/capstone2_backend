package Capstone.capstoneProject.dto.Usages;

import Capstone.capstoneProject.enums.UsageCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ReceiptItemPatchDTO {
    private Long usageHistoryId; // 필수
    private String name;
    private BigDecimal price;
    private UsageCategory category;
    private Integer amount;
}

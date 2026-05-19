package Capstone.capstoneProject.dto.usage;

import Capstone.capstoneProject.entity.usage.UsageHistory;
import Capstone.capstoneProject.enums.HistoryType;
import Capstone.capstoneProject.enums.UsageCategory;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecentTransactionDTO {
    private String name;
    private HistoryType historyType;
    private BigDecimal price;
    private UsageCategory category;
    private Integer amount;
    private String proDate;

    public static RecentTransactionDTO from(UsageHistory history) {
        return RecentTransactionDTO.builder()
                .name(history.getName())
                .historyType(history.getHistoryType())
                .price(history.getPrice())
                .category(history.getCategory())
                .amount(history.getAmount())
                .proDate(history.getProDate().toString())
                .build();
    }
}

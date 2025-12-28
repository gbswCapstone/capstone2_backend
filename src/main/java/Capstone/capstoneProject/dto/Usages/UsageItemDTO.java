package Capstone.capstoneProject.dto.Usages;

import Capstone.capstoneProject.entity.UsageHistory;
import Capstone.capstoneProject.enums.HistoryType;
import Capstone.capstoneProject.enums.UsageCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UsageItemDTO {
    private String name;
    private BigDecimal price;
    private UsageCategory category;
    private HistoryType historyType;
    private String proDate;

    public static UsageItemDTO from(UsageHistory usageHistory) {
        return UsageItemDTO.builder()
                .name(usageHistory.getName())
                .price(usageHistory.getPrice())
                .category(usageHistory.getCategory())
                .historyType(usageHistory.getHistoryType())
                .proDate(usageHistory.getProDate().toString())
                .build();
    }
}

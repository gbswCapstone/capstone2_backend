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
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UsageShareDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private LocalDate proDate; // 사용내역 날짜
    private UsageCategory category;
    private HistoryType historyType;
    private int amount;

    public static UsageShareDTO from(UsageHistory usageHistory) {
       return UsageShareDTO.builder()
               .id(usageHistory.getId())
               .name(usageHistory.getName())
               .price(usageHistory.getPrice())
               .proDate(usageHistory.getProDate())
               .category(usageHistory.getCategory())
               .historyType(usageHistory.getHistoryType())
               .amount(usageHistory.getAmount())
               .build();
    }
}

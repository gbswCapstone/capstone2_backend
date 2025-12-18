package Capstone.capstoneProject.dto.Usages;

import Capstone.capstoneProject.entity.UsageHistory;
import Capstone.capstoneProject.enums.UsageCategory;
import lombok.*;
import java.math.BigDecimal;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceiptListDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private UsageCategory category;
    private int amount;

    public static ReceiptListDTO from(UsageHistory usageHistory) {
        return ReceiptListDTO.builder()
                .id(usageHistory.getId())
                .name(usageHistory.getName())
                .price(usageHistory.getPrice())
                .category(usageHistory.getCategory())
                .amount(usageHistory.getAmount())
                .build();
    }
}

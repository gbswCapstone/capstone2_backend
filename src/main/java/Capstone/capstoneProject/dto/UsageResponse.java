package Capstone.capstoneProject.dto;

import Capstone.capstoneProject.entity.UsageHistory;
import Capstone.capstoneProject.enums.HistoryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UsageResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private LocalDate proDate;
    private LocalDateTime createdAt;
    private String category;
    private HistoryType historyType;

    public UsageResponse(UsageHistory usageHistory) {
        this.id = usageHistory.getId();
        this.name = usageHistory.getName();
        this.price = usageHistory.getPrice();
        this.proDate = usageHistory.getProDate();
        this.category = usageHistory.getCategory();
        this.historyType = usageHistory.getHistoryType();
        this.createdAt = usageHistory.getCreatedAt();
    }
}

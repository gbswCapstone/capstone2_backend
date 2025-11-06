package Capstone.capstoneProject.dto;

import Capstone.capstoneProject.entity.UsageHistory;
import Capstone.capstoneProject.enums.HistoryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class UsageResponse {
    private Long id;
    private String userEmail;
    private String name;
    private BigDecimal price;
    private LocalDateTime proDate;
    private String category;
    private HistoryType historyType;

    public UsageResponse(UsageHistory usageHistory) {
        this.id = usageHistory.getId();
        this.userEmail = usageHistory.getUsers().getEmail();
        this.name = usageHistory.getName();
        this.price = usageHistory.getPrice();
        this.proDate = usageHistory.getProDate();
        this.category = usageHistory.getCategory();
        this.historyType = usageHistory.getHistoryType();
    }
}

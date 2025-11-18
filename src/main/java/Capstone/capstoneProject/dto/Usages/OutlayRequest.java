package Capstone.capstoneProject.dto.Usages;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
public class OutlayRequest {
    private String productName;
    private BigDecimal price;
    private LocalDate proDate;
    private int amount;
}



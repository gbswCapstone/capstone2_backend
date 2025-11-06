package Capstone.capstoneProject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
public class OutlayRequest {
    private String productName;
    private BigDecimal price;
    private LocalDateTime proDate;
}



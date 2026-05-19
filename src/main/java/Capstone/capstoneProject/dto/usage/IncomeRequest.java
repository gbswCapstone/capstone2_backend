package Capstone.capstoneProject.dto.usage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
public class IncomeRequest {
    private String importer;
    private BigDecimal price;
    private LocalDate proDate;
    private int amount;

}

package Capstone.capstoneProject.dto.Missions;

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
public class MissionCreate {
    private String title;
    private String rule;
    private BigDecimal goalAmount;
    private LocalDate startDate;
    private LocalDate endDate;

}

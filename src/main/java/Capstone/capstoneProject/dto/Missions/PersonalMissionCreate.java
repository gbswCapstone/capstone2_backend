package Capstone.capstoneProject.dto.Missions;

import Capstone.capstoneProject.enums.PeriodType;
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
public class PersonalMissionCreate {
    private String title;
    private String rule;
    private String description; // null허용
    private BigDecimal targetAmount;
    private LocalDate startDate;
    private LocalDate endDate;

}

package Capstone.capstoneProject.dto.mission;

import Capstone.capstoneProject.enums.MissionType;
import Capstone.capstoneProject.enums.UsageCategory;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    private MissionType missionType;
    private UsageCategory category;
    private int mexInt;
    private BigDecimal goalAmount;
    private LocalDate startDate;
    private LocalDate endDate;

}

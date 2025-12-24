package Capstone.capstoneProject.dto.Missions;

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
    private String rule;
    private BigDecimal goalAmount;
    private LocalDate startDate;
    private LocalDate endDate;

}

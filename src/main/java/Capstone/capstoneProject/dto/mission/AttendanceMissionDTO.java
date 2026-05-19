package Capstone.capstoneProject.dto.mission;

import Capstone.capstoneProject.enums.MissionStatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@Builder
public class AttendanceMissionDTO {
    private Long id;
    private String title;
    private MissionStatusType status;
    private LocalDate startDate;
    private LocalDate endDate;
}

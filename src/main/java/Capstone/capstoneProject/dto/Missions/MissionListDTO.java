package Capstone.capstoneProject.dto.Missions;

import Capstone.capstoneProject.entity.Missions.Missions;
import Capstone.capstoneProject.entity.Missions.UserMissions;
import Capstone.capstoneProject.enums.MissionStatusType;
import Capstone.capstoneProject.enums.MissionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bouncycastle.pqc.legacy.math.linearalgebra.BigEndianConversions;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MissionListDTO {
    private Long id;
    private MissionType missionType;
    private String title;
    private String rule;
    private MissionStatusType status;
    private int experience;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal goalAmount;
    private BigDecimal currentAmount; // 지금 현재

    public static MissionListDTO from(Missions missions, UserMissions userMissions, BigDecimal currentAmount) {
        return MissionListDTO.builder()
                .id(missions.getId())
                .missionType(missions.getMissionType())
                .title(missions.getTitle())
                .rule(missions.getRule())
                .status(userMissions.getMissionStatusType())
                .experience(missions.getExperience())
                .startDate(missions.getStartDate())
                .endDate(missions.getEndDate())
                .goalAmount(missions.getGoalAmount())
                .currentAmount(currentAmount)
                .build();
    }
}

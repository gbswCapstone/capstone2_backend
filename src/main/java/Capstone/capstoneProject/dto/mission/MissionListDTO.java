package Capstone.capstoneProject.dto.mission;

import Capstone.capstoneProject.entity.mission.Missions;
import Capstone.capstoneProject.entity.mission.UserMissions;
import Capstone.capstoneProject.enums.MissionStatusType;
import Capstone.capstoneProject.enums.MissionType;
import Capstone.capstoneProject.enums.UsageCategory;
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
    private UsageCategory category;
    private MissionStatusType status;
    private int experience;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal goalAmount; // null일 수도 있음
    private BigDecimal currentAmount; // 지금 현재

    public static MissionListDTO from(Missions missions, UserMissions userMissions, BigDecimal currentAmount) {
        return MissionListDTO.builder()
                .id(missions.getId())
                .missionType(missions.getMissionType())
                .title(missions.getTitle())
                .category(missions.getCategory())
                .status(userMissions.getMissionStatusType())
                .experience(missions.getExperience())
                .startDate(missions.getStartDate())
                .endDate(missions.getEndDate())
                .goalAmount(missions.getGoalAmount())
                .currentAmount(currentAmount)
                .build();
    }
}

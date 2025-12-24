package Capstone.capstoneProject.dto;

import Capstone.capstoneProject.dto.Missions.MissionResponse;
import Capstone.capstoneProject.entity.Missions.Missions;
import Capstone.capstoneProject.entity.Missions.UserMissions;
import Capstone.capstoneProject.enums.MissionStatusType;
import Capstone.capstoneProject.enums.MissionType;
import Capstone.capstoneProject.enums.UsageCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MonthGoalMissionDTO {
    private boolean isSet;
    private Long id;
    private MissionType missionType;
    private String title;
    private MissionStatusType status;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;
    private BigDecimal currentPrice; // 현재 지출 금액
    private BigDecimal goalAmount; // 목표 금액
    private BigDecimal remainingAmount; // 목표까지 남은 금액

    public static MonthGoalMissionDTO from(boolean isSet, Missions missions, UserMissions userMissions, BigDecimal currentPrice, BigDecimal remainingAmount) {
        return MonthGoalMissionDTO.builder()
                .isSet(isSet)
                .id(missions.getId())
                .missionType(missions.getMissionType())
                .title(missions.getTitle())
                .status(userMissions.getMissionStatusType())
                .startDate(missions.getStartDate())
                .endDate(missions.getEndDate())
                .createdAt(missions.getCreatedAt())
                .currentPrice(currentPrice)
                .remainingAmount(remainingAmount)
                .goalAmount(missions.getGoalAmount())
                .build();
    }
}

package Capstone.capstoneProject.dto.Missions;

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
public class MissionResponse {
    private Long id;
    private MissionType missionType; // ZERO_SPENDING 등
    private String title;
    private UsageCategory category; // 식비, 교통비 등
    private MissionStatusType status;
    private int experience;

    // 성공 횟수 관련 추가
    private int currentStreak;      // 현재 0~4번 성공 상태
    private int successThreshold;   // 5번 성공 시 보너스 (기존 maxInt)

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;

    private Long challengeId;
    private BigDecimal goalAmount;

    public static MissionResponse from(Missions missions, UserMissions userMissions) {
        return MissionResponse.builder()
                .id(missions.getId())
                .missionType(missions.getMissionType())
                .title(missions.getTitle())
                .category(missions.getCategory())
                .status(userMissions.getMissionStatusType())
                .experience(missions.getExperience())
                // UserMissions에 저장된 현재 연속 성공 횟수
                .currentStreak(userMissions.getCurrentStreak())
                // Missions에 설정된 목표 횟수 (예: 5)
                .successThreshold(missions.getMaxInt())
                .startDate(missions.getStartDate())
                .endDate(missions.getEndDate())
                .createdAt(missions.getCreatedAt())
                .challengeId(missions.getChallenges() != null ? missions.getChallenges().getId() : null)
                .goalAmount(missions.getGoalAmount())
                .build();
    }
}

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
    private MissionType missionType;
    private String title;
    private UsageCategory category;
    private MissionStatusType status;
    private int experience; // 미션성공하면 받게 되는 경험치
    private LocalDate startDate; // 시작날짜
    private LocalDate endDate; // 끝날짜
    private LocalDateTime createdAt;

    // 있을 수도 있고 없을 수도 있음
    private Long challengeId; // 챌린지 미션일 경우
    private BigDecimal targetAmount; // 목표금액 미션일 경우


    public static MissionResponse from(Missions missions, UserMissions userMissions) {
        return MissionResponse.builder()
                .id(missions.getId())
                .missionType(missions.getMissionType())
                .title(missions.getTitle())
                .category(missions.getCategory())
                .status(userMissions.getMissionStatusType())
                .experience(missions.getExperience())
                .startDate(missions.getStartDate())
                .endDate(missions.getEndDate())
                .createdAt(missions.getCreatedAt())
                .challengeId(missions.getChallenges() != null ? missions.getChallenges().getId() : null)
                .targetAmount(missions.getGoalAmount())
                .build();
    }
}

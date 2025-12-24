package Capstone.capstoneProject.dto;

import Capstone.capstoneProject.enums.MissionStatusType;
import Capstone.capstoneProject.enums.MissionType;
import Capstone.capstoneProject.enums.UsageCategory;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class MonthGoalMissionDTO {
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
}

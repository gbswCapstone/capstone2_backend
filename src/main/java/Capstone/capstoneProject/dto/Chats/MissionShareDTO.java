package Capstone.capstoneProject.dto.Chats;

import Capstone.capstoneProject.entity.Missions.Missions;
import Capstone.capstoneProject.entity.challenges.Challenges;
import Capstone.capstoneProject.enums.HistoryType;
import Capstone.capstoneProject.enums.MissionType;
import Capstone.capstoneProject.enums.UsageCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MissionShareDTO {
    private Long id; // 미션아이디
    private String title;
    private MissionType missionType;
    private String rule;
    private BigDecimal goalAmount; // null일 수도 있음
    private int experience;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static MissionShareDTO from(Missions missions) {
        return MissionShareDTO.builder()
                .id(missions.getId())
                .title(missions.getTitle())
                .missionType(missions.getMissionType())
                .rule(missions.getRule())
                .goalAmount(missions.getGoalAmount())
                .experience(missions.getExperience())
                .startDate(missions.getStartDate())
                .endDate(missions.getEndDate())
                .createdAt(missions.getCreatedAt())
                .updatedAt(missions.getUpdatedAt())
                .build();
    }


}

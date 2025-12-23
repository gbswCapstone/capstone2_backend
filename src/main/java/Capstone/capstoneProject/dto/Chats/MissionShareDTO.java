package Capstone.capstoneProject.dto.Chats;

import Capstone.capstoneProject.entity.Missions.Missions;
import Capstone.capstoneProject.enums.MissionType;
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
public class MissionShareDTO {
    private Long id; // 미션아이디
    private String title;
    private String rule;
    private BigDecimal goalAmount; // null일 수도 있음
    private LocalDate startDate;
    private LocalDate endDate;
    private int currentParticipants; // 현재 미션 참여 인원

    public static MissionShareDTO from(Missions missions, int currentParticipants) {
        return MissionShareDTO.builder()
                .id(missions.getId())
                .title(missions.getTitle())
                .rule(missions.getRule())
                .goalAmount(missions.getGoalAmount())
                .startDate(missions.getStartDate())
                .endDate(missions.getEndDate())
                .currentParticipants(currentParticipants)
                .build();
    }


}

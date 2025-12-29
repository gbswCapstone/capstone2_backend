package Capstone.capstoneProject.service;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MissionCompletedEvent {
    private final Long userId;
    private final Long missionId;
}

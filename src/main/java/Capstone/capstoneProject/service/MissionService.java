package Capstone.capstoneProject.service;

import Capstone.capstoneProject.dto.Missions.MissionResponse;
import Capstone.capstoneProject.dto.Missions.PersonalMissionCreate;
import Capstone.capstoneProject.entity.Missions.MissionLevels;
import Capstone.capstoneProject.entity.Missions.Missions;
import Capstone.capstoneProject.entity.Missions.UserMissions;
import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.enums.MissionStatusType;
import Capstone.capstoneProject.enums.MissionType;
import Capstone.capstoneProject.enums.PeriodType;
import Capstone.capstoneProject.repository.MissionLevelRepository;
import Capstone.capstoneProject.repository.MissionRepository;
import Capstone.capstoneProject.repository.UserMissionRepository;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
@Transactional
public class MissionService {
    private final AuthenticatedUserUtils authenticatedUserUtils;
    private final MissionRepository missionRepository;
    private final UserMissionRepository userMissionRepository;
    private final MissionLevelRepository missionLevelRepository;

    public MissionResponse createPersonalMission(PersonalMissionCreate request) {
        Users user = authenticatedUserUtils.getCurrentUser();
        String description;
        if (request.getDescription().isEmpty()) {
            description = "이 미션을 완료하고 경험치를 획득해보세요!";
        } else {
            description = request.getDescription();
        }
        Missions missions = Missions.builder()
                .challenges(null)
                .missionType(MissionType.CUSTOM)
                .title(request.getTitle())
                .description(description)
                .goalAmount(request.getTargetAmount())
                .experience(20)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();
        missionRepository.save(missions);

        MissionLevels missionLevels = MissionLevels.builder()
                .missions(missions)
                .periodType(PeriodType.DAY)
                .experience(missions.getExperience())
                .rule(request.getRule())
                .build();
        missionLevelRepository.save(missionLevels);

        UserMissions userMissions = UserMissions.builder()
                .users(user)
                .missions(missions)
                .missionStatusType(MissionStatusType.PROGRESS)
                .progressPercentage(0)
                .completedAt(null)
                .experience(missions.getExperience())
                .build();
        userMissionRepository.save(userMissions);

        return MissionResponse.from(missions, userMissions, missionLevels);
    }

}

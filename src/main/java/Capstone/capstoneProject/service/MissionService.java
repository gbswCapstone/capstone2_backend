package Capstone.capstoneProject.service;

import Capstone.capstoneProject.dto.Missions.MissionResponse;
import Capstone.capstoneProject.dto.Missions.MissionCreate;
import Capstone.capstoneProject.entity.Missions.Missions;
import Capstone.capstoneProject.entity.Missions.UserMissions;
import Capstone.capstoneProject.entity.Users.Users;
import Capstone.capstoneProject.enums.MissionStatusType;
import Capstone.capstoneProject.enums.MissionType;
import Capstone.capstoneProject.enums.PeriodType;
import Capstone.capstoneProject.repository.*;
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


//
//    public MissionResponse createPersonalMission(MissionCreate request) {
//        Users user = authenticatedUserUtils.getCurrentUser();
//
//        Missions missions = Missions.builder()
//                .challenges(null)
//                .missionType(MissionType.CUSTOM)
//                .title(request.getTitle())
//                .rule(request.getRule())
//                .goalAmount(request.getGoalAmount())
//                .experience(20)
//                .startDate(request.getStartDate())
//                .endDate(request.getEndDate())
//                .build();
//        missionRepository.save(missions);
//
//        MissionLevels missionLevels = MissionLevels.builder()
//                .missions(missions)
//                .periodType(PeriodType.DAY)
//                .experience(missions.getExperience())
//                .build();
//        missionLevelRepository.save(missionLevels);
//
//        UserMissions userMissions = UserMissions.builder()
//                .users(user)
//                .missions(missions)
//                .missionStatusType(MissionStatusType.PROGRESS)
//                .completedAt(null)
//                .experience(missions.getExperience())
//                .build();
//        userMissionRepository.save(userMissions);
//
//        return MissionResponse.from(missions, userMissions, missionLevels);
//    }



//    public List<MissionListDTO> getMissions(SortType sortType) {
//        Users user = authenticatedUserUtils.getCurrentUser();
//        List<UserMissions> userMissions = userMissionRepository.findAllByUsers(user);
//        List<Missions> missions = missionRepository.findAllById(userMissions.get().getMissions().getId());
//
//
//        // 현재 진행률
//        BigDecimal currentAmount;
//        return MissionListDTO.from(missions, userMissions, currentAmount);
//    }

}

package Capstone.capstoneProject.scheduler;


import Capstone.capstoneProject.entity.mission.Missions;
import Capstone.capstoneProject.entity.mission.UserMissions;
import Capstone.capstoneProject.entity.user.Users;
import Capstone.capstoneProject.enums.MissionStatusType;
import Capstone.capstoneProject.enums.MissionType;
import Capstone.capstoneProject.repository.mission.MissionRepository;
import Capstone.capstoneProject.repository.mission.UserMissionRepository;
import Capstone.capstoneProject.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyMissionScheduler {
    private final UserRepository userRepository;
    private final MissionRepository missionRepository;
    private final UserMissionRepository userMissionRepository;

    // 필수 출석체크 미션
    @Scheduled(cron = "0 0 0 * * *")
    public void createDailyAttendanceMissions() {
        List<Users> users = userRepository.findAll();
        Missions template = missionRepository
                .findByMissionType(MissionType.ATTENDANCE_CHECK)
                .orElseThrow();

        for (Users user : users) {
            boolean exists =
                    userMissionRepository
                            .existsByUsersAndMissionsAndMissionStatusType(
                                    user,
                                    template,
                                    MissionStatusType.PROGRESS
                            );

            if (!exists) {
                UserMissions um = UserMissions.builder()
                        .users(user)
                        .missions(template)
                        .missionStatusType(MissionStatusType.PROGRESS)
                        .currentStreak(0)
                        .build();

                userMissionRepository.save(um);
            }
        }
    }

}

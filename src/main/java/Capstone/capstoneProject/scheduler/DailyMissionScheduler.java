package Capstone.capstoneProject.scheduler;


import Capstone.capstoneProject.entity.Missions.Missions;
import Capstone.capstoneProject.entity.Missions.UserMissions;
import Capstone.capstoneProject.entity.Users.Users;
import Capstone.capstoneProject.enums.MissionStatusType;
import Capstone.capstoneProject.enums.MissionType;
import Capstone.capstoneProject.repository.MissionRepository;
import Capstone.capstoneProject.repository.UserMissionRepository;
import Capstone.capstoneProject.repository.UserRepository;
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
    @Scheduled(cron = "0 0 0 * * *") // 매일 0시
    public void createDailyAttendanceMissions() {
        List<Users> users = userRepository.findAll();
        Missions template = missionRepository.findByMissionType(MissionType.ATTENDANCE_CHECK)
                .orElseThrow();

        LocalDate today = LocalDate.now();

        for (Users user : users) {
            boolean exists = userMissionRepository.existsByUsersAndMissionsAndMissions_StartDate(user, template, today);
            if (!exists) {
                UserMissions um = UserMissions.builder()
                        .users(user)
                        .missions(template)
                        .missionStatusType(MissionStatusType.PROGRESS)
                        .currentStreak(0)
                        .createdAt(LocalDateTime.now())
                        .build();
                userMissionRepository.save(um);
            }
        }
    }

}

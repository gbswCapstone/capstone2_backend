package Capstone.capstoneProject.init;

import Capstone.capstoneProject.entity.Missions.Missions;
import Capstone.capstoneProject.enums.MissionType;
import Capstone.capstoneProject.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class MissionInitializer implements CommandLineRunner {
    private final MissionRepository missionRepository;
    @Override
    public void run(String... args) {

        missionRepository.findByMissionType(MissionType.ATTENDANCE_CHECK)
                .orElseGet(() -> {

                    LocalDate startDate = LocalDate.of(2000, 1, 1);
                    LocalDate endDate = LocalDate.of(2099, 12, 31);

                    Missions mission = Missions.builder()
                            .missionType(MissionType.ATTENDANCE_CHECK)
                            .title("출석 체크")
                            .experience(10)
                            .startDate(startDate)
                            .endDate(endDate)
                            .build();

                    return missionRepository.save(mission);
                });
    }
}

package Capstone.capstoneProject.repository.mission;

import Capstone.capstoneProject.entity.mission.Missions;
import Capstone.capstoneProject.entity.challenge.Challenges;
import Capstone.capstoneProject.enums.MissionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MissionRepository extends JpaRepository<Missions, Long> {
    List<Missions> findByChallenges(Challenges challenge);
    Optional<Missions> findByMissionType(MissionType missionType);
}

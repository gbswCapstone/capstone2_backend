package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.Missions.Missions;
import Capstone.capstoneProject.entity.challenges.Challenges;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MissionRepository extends JpaRepository<Missions, Long> {
    List<Missions> findByChallenges(Challenges challenge);
}

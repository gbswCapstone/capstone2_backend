package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.Missions.Missions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepository extends JpaRepository<Missions, Long> {
}

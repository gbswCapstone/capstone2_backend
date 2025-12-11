package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.Missions.UserMissions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMissionRepository extends JpaRepository<UserMissions, Long> {
}

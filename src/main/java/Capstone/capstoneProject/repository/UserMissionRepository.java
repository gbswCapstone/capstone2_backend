package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.Missions.Missions;
import Capstone.capstoneProject.entity.Missions.UserMissions;
import Capstone.capstoneProject.entity.Users.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserMissionRepository extends JpaRepository<UserMissions, Long> {
    boolean existsByUsersAndMissions(Users user, Missions missions);
    List<UserMissions> findAllByUsers(Users user);

    // 미션 참여인원 계산
    int countByMissionsId(Long missionId);
}

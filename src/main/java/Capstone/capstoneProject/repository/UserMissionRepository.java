package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.Missions.Missions;
import Capstone.capstoneProject.entity.Missions.UserMissions;
import Capstone.capstoneProject.entity.Users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface UserMissionRepository extends JpaRepository<UserMissions, Long> {
    boolean existsByUsersAndMissions(Users user, Missions missions);
    List<UserMissions> findAllByUsers(Users user);


    // 출석
    @Query("""
    SELECT um
    FROM UserMissions um
    JOIN FETCH um.missions m
    WHERE um.users.id = :userId
      AND um.missionStatusType = 'PROGRESS'
      AND m.missionType = 'ATTENDANCE_CHECK'
      AND :today BETWEEN m.startDate AND m.endDate
""")
    List<UserMissions> findTodayAttendanceMissions(
            @Param("userId") Long userId,
            @Param("today") LocalDate today
    );



    @Query("""
    SELECT um
    FROM UserMissions um
    JOIN FETCH um.missions m
    WHERE um.users.id = :userId
      AND um.missionStatusType = 'PROGRESS'
      AND :today BETWEEN m.startDate AND m.endDate
""")
    List<UserMissions> findActiveMissions(
            @Param("userId") Long userId,
            @Param("today") LocalDate today
    );

    // 미션 참여인원 계산
    int countByMissionsId(Long missionId);
}

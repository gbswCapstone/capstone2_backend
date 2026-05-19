package Capstone.capstoneProject.repository.mission;

import Capstone.capstoneProject.entity.mission.Missions;
import Capstone.capstoneProject.entity.mission.UserMissions;
import Capstone.capstoneProject.entity.user.Users;
import Capstone.capstoneProject.enums.MissionStatusType;
import Capstone.capstoneProject.enums.MissionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserMissionRepository extends JpaRepository<UserMissions, Long> {
    boolean existsByUsersAndMissions(Users user, Missions missions);

    Optional<UserMissions>
    findTopByUsersAndMissions_MissionTypeOrderByCreatedAtDesc(
            Users users,
            MissionType missionType
    );
    boolean existsByUsersAndMissions_MissionType(Users users, MissionType missionType);
    boolean existsByUsersAndMissionsAndMissionStatusType(
            Users users,
            Missions missions,
            MissionStatusType missionStatusType
    );

    // 이번달 목표금액 미션 해놓았는지 조회
    Optional<UserMissions> findByUsersAndMissions_MissionType(Users user, MissionType missionType);

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

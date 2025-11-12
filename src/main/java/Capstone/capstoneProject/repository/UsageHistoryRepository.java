package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.UsageHistory;
import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.enums.HistoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface UsageHistoryRepository extends JpaRepository<UsageHistory, Long> {


    @Query("SELECT u FROM UsageHistory u " +
            "WHERE u.users = :user " +
            "AND (:type = 'ALL' OR u.historyType = :type) " +
            "AND u.proDate BETWEEN :start AND :end " +
            "ORDER BY u.createdAt DESC")
    List<UsageHistory> findByUserAndTypeDTO(
            @Param("user") Users user,
            @Param("type") HistoryType type,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end);



}

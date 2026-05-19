package Capstone.capstoneProject.repository.usage;

import Capstone.capstoneProject.entity.usage.UsageHistory;
import Capstone.capstoneProject.entity.user.Users;
import Capstone.capstoneProject.enums.HistoryType;
import Capstone.capstoneProject.enums.UsageCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UsageHistoryRepository extends JpaRepository<UsageHistory, Long>, UsageHistoryCustom {

    @Query("""
    SELECT COALESCE(SUM(uh.price * uh.amount), 0)
    FROM UsageHistory uh
    WHERE uh.users.id = :userId
      AND uh.category = :category
      AND uh.historyType = :historyType
      AND uh.proDate BETWEEN :startDate AND :endDate
""")
    BigDecimal sumAmount(
            @Param("userId") Long userId,
            @Param("category") UsageCategory category,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("historyType") HistoryType historyType
    );




    // 지출 횟수 계산 SPENDING_COUNT
    @Query("SELECT COUNT(uh) FROM UsageHistory uh " +
            "WHERE uh.users = :user " +
            "AND uh.historyType = 'OUTLAY' " + // 지출 내역만
            "AND uh.proDate BETWEEN :startDate AND :endDate")
    Long countUsageHistory(
            @Param("user") Users user,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // 무지출
    @Query("""
    SELECT COALESCE(SUM(uh.price * uh.amount), 0)
    FROM UsageHistory uh
    WHERE uh.users = :user
      AND (:category IS NULL OR uh.category = :category)
      AND uh.historyType = 'OUTLAY'
      AND uh.proDate BETWEEN :startDate AND :endDate
""")
    BigDecimal calculateTotalSpending(
            @Param("user") Users user,
            @Param("category") UsageCategory category,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );



    // 무지출 일수 계산 (해당 기간 중 지출 내역이 없는 날의 수)
    @Query("SELECT COUNT(DISTINCT uh.proDate) FROM UsageHistory uh " +
            "WHERE uh.users = :user " +
            "AND uh.historyType = 'OUTLAY' " +
            "AND uh.proDate BETWEEN :startDate AND :endDate")
    Long countSpendingDays(Users user, LocalDate startDate, LocalDate endDate);


    Optional<UsageHistory> findByIdAndUsers(Long id, Users user);
    List<UsageHistory> findAllByIdInAndUsers(List<Long> ids, Users user);
    List<UsageHistory> findTop2ByUsersOrderByProDateDesc(Users user);
    List<UsageHistory> findAllByUsers(Users users);

    // 이번달 사용내역 전체 조회
    @Query("SELECT u FROM UsageHistory u WHERE u.users = :user AND u.proDate BETWEEN :start AND :end ORDER BY u.proDate DESC")
    List<UsageHistory> findAllByUsersForCurrentMonth(@Param("user") Users user,
                                                     @Param("start") LocalDate start,
                                                     @Param("end") LocalDate end);

    // 사용자의 이번달 지출 가격합산
    @Query("""
    SELECT COALESCE(SUM(u.price * u.amount), 0)
    FROM UsageHistory u
    WHERE u.users = :user
      AND u.historyType = :historyType
      AND u.proDate BETWEEN :start AND :end
""")
    BigDecimal sumOutlayForCurrentMonth(
            @Param("user") Users user,
            @Param("historyType") HistoryType historyType,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    // 이번주 사용내역 전체 조회
    @Query("SELECT u FROM UsageHistory u WHERE u.users = :user AND u.proDate BETWEEN :start AND :end ORDER BY u.proDate DESC")
    List<UsageHistory> findAllByUsersForCurrentWeek(@Param("user") Users user,
                                                    @Param("start") LocalDate start,
                                                    @Param("end") LocalDate end);

    // 사용자의 총 지출 가격
    @Query("SELECT SUM(u.price) FROM UsageHistory u WHERE u.users.id = :userId AND u.historyType = 'OUTLAY'")
    List<Integer> sumOutlayByUser(@Param("userId") Long userId, Pageable pageable);

    // 사용자의 총 수입 가격
    @Query("SELECT SUM(u.price) FROM UsageHistory u WHERE u.users.id = :userId AND u.historyType = 'INCOME'")
    List<Integer> sumIncomeByUser(@Param("userId") Long userId, Pageable pageable);

    // 지출내역에서 가장 많은 카테고리(수량기준)
    @Query("SELECT u.category FROM UsageHistory u WHERE u.users.id = :userId AND u.historyType = 'OUTLAY' GROUP BY u.category ORDER BY SUM(u.amount) DESC")
    List<UsageCategory> findTopCategoryByOutlay(@Param("userId") Long userId, Pageable pageable);

    // 수입내역에서 가장 많은 카테고리(수량기준)
    @Query("SELECT u.category FROM UsageHistory u WHERE u.users.id = :userId AND u.historyType = 'INCOME' GROUP BY u.category ORDER BY SUM(u.amount) ASC")
    List<UsageCategory> findTopCategoryByIncome(@Param("userId") Long userId, Pageable pageable);

    // 가장 많은 지출 상품명(수량기준)
    @Query("SELECT u.name FROM UsageHistory u WHERE u.users.id = :userId AND u.historyType = 'OUTLAY' GROUP BY u.name ORDER BY SUM(u.amount) DESC")
    List<String> findMostOutlayItemName(@Param("userId") Long userId, Pageable pageable);

    // 가장 많은 수입 수입처(수량기준)
    @Query("SELECT u.name FROM UsageHistory u WHERE u.users.id = :userId AND u.historyType = 'INCOME' GROUP BY u.name ORDER BY SUM(u.amount) DESC")
    List<String> findtopIncomeImporter(@Param("userId") Long userId, Pageable pageable);

    // 가장 비싼 지출 상품명(가격기준)
    @Query("SELECT u.name FROM UsageHistory u WHERE u.users.id = :userId AND u.historyType = 'OUTLAY' ORDER BY u.price DESC")
    List<String> findHighestOutlayItemName(@Param("userId") Long userId, Pageable pageable);

    // 가장 비싼 수입 수입처(가격기준)
    @Query("SELECT u.name FROM UsageHistory u WHERE u.users.id = :userId AND u.historyType = 'INCOME' ORDER BY u.price DESC")
    List<String> findhighestIncomeImporter(@Param("userId") Long userId, Pageable pageable);

    // 제일 많은 지출 카테고리(수량)
    @Query("SELECT u.category, COUNT(u) FROM UsageHistory u " +
            "WHERE u.users.id = :userId AND u.historyType = 'OUTLAY' " +
            "GROUP BY u.category")
    List<Object[]> countOutlayCategory(@Param("userId") Long userId);

    // 제일 많은 수입 카테고리(수량)
    @Query("SELECT u.category, COUNT(u) FROM UsageHistory u " +
            "WHERE u.users.id = :userId AND u.historyType = 'INCOME' " +
            "GROUP BY u.category")
    List<Object[]> countIncomeCategory(@Param("userId") Long userId);


}

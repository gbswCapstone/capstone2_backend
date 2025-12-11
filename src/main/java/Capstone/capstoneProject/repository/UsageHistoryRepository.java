package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.UsageHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsageHistoryRepository extends JpaRepository<UsageHistory, Long>, UsageHistoryCustom {

    // 사용자의 총 지출 가격
    @Query("SELECT SUM(u.price) FROM UsageHistory u WHERE u.users.id = :userId AND u.historyType = 'OUTLAY'")
    List<Integer> sumOutlayByUser(@Param("userId") Long userId, Pageable pageable);

    // 사용자의 총 수입 가격
    @Query("SELECT SUM(u.price) FROM UsageHistory u WHERE u.users.id = :userId AND u.historyType = 'INCOME'")
    List<Integer> sumIncomeByUser(@Param("userId") Long userId, Pageable pageable);

    // 지출내역에서 가장 많은 카테고리(수량기준)
    @Query("SELECT u.category FROM UsageHistory u WHERE u.users.id = :userId AND u.historyType = 'OUTLAY' GROUP BY u.category ORDER BY SUM(u.amount) DESC")
    List<String> findTopCategoryByOutlay(@Param("userId") Long userId, Pageable pageable);

    // 지출내역에서 가장 적은 카테고리(수량기준)
    @Query("SELECT u.category FROM UsageHistory u WHERE u.users.id = :userId AND u.historyType = 'OUTLAY' GROUP BY u.category ORDER BY SUM(u.amount) ASC")
    List<String> findLeastCategoryByOutlay(@Param("userId") Long userId, Pageable pageable);

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

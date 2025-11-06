package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.UsageHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsageHistoryRepository extends JpaRepository<UsageHistory, Long> {
}

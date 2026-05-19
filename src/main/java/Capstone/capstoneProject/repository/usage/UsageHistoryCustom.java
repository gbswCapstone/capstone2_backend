package Capstone.capstoneProject.repository.usage;

import Capstone.capstoneProject.entity.usage.UsageHistory;
import Capstone.capstoneProject.enums.HistoryType;
import Capstone.capstoneProject.enums.UsageSortType;

import java.time.LocalDate;
import java.util.List;

public interface UsageHistoryCustom {
    List<UsageHistory> searchDynamic(Long userId, HistoryType type, LocalDate startDate, LocalDate endDate, UsageSortType sortType);
}


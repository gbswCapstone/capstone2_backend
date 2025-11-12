package Capstone.capstoneProject.dto;

import Capstone.capstoneProject.enums.HistoryType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UsageSearchTypeDTO {
    private HistoryType type;      // INCOME / EXPENSE / ALL
    private LocalDate startDate;   // 시작일
    private LocalDate endDate;     // 종료일
    private Integer year;          // 연도별 조회
    private Integer month;         // 월별 조회
    private Integer week;          // 주별 조회
    private String preset;         // thisWeek, lastWeek, today

    public UsageSearchTypeDTO() {
        this.type = HistoryType.ALL;
    }
}

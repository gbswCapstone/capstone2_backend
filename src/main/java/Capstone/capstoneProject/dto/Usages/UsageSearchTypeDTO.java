package Capstone.capstoneProject.dto.Usages;

import Capstone.capstoneProject.enums.HistoryType;
import Capstone.capstoneProject.enums.PresetType;
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
    private PresetType presetType;

    public UsageSearchTypeDTO() {
        this.type = HistoryType.ALL;
    }
}

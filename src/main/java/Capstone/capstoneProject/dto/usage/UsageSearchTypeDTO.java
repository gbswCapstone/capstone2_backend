package Capstone.capstoneProject.dto.usage;

import Capstone.capstoneProject.enums.HistoryType;
import Capstone.capstoneProject.enums.PresetType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;@Getter
@Setter
public class UsageSearchTypeDTO {

    @Schema(description = "조회 구분 (INCOME: 수입, OUTLAY: 지출, ALL: 전체)", example = "ALL")
    private HistoryType type;

    @Schema(description = "조회 시작 날짜 (yyyy-MM-dd)", example = "2025-01-01")
    private LocalDate startDate;

    @Schema(description = "조회 종료 날짜 (yyyy-MM-dd)", example = "2025-01-31")
    private LocalDate endDate;

    @Min(value = 2000, message = "년도는 2000 이상으로 입력해주세요. 예: 2025")
    @Schema(description = "연도별 조회 시 지정 (2000~)", example = "2025")
    private Integer year;

    @Min(value = 1, message = "월은 1~12 사이 숫자로 입력해주세요. 예: 6")
    @Max(value = 12, message = "월은 1~12 사이 숫자로 입력해주세요. 예: 6")
    @Schema(description = "월별 조회 시 지정 (1~12)", example = "1")
    private Integer month;

    @Min(value = 1, message = "해당 월에 없는 주차는 에러처리 됩니다.")
    @Max(value = 5, message = "해당 월에 없는 주차는 에러처리 됩니다.")
    @Schema(description = "월별 주별 조회 시 지정 (1~5)", example = "3")
    private Integer week;

    @Schema(description = "간편조회 타입(TODAY: 오늘, LAST_WEEK: 이번주, LAST_MONTH: 이번달)", example = "LAST_MONTH")
    private PresetType presetType;

}



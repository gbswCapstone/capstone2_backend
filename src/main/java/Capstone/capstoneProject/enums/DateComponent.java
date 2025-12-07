package Capstone.capstoneProject.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public enum DateComponent {

    @Min(2000)
    @Schema(description = "연도별 조회 시 지정 (2000~)", example = "2025")
    YEAR,

    @Min(1)
    @Max(12)
    @Schema(description = "월별 조회 시 지정 (1~12)", example = "1")
    MONTH,

    @Min(1)
    @Max(5)
    @Schema(description = "월별 주별 조회 시 지정 (1~5)", example = "10")
    WEEK
}

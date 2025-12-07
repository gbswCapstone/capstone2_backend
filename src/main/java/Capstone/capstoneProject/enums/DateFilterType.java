package Capstone.capstoneProject.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public enum DateFilterType {
    @Schema(description = "조회 시작 날짜 (yyyy-MM-dd)", example = "2025-01-01")
    STARTDATE,

    @Schema(description = "조회 종료 날짜 (yyyy-MM-dd)", example = "2025-01-31")
    ENDDATE
}

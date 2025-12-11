package Capstone.capstoneProject.dto.Usages;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Schema(description = "유저 전체기간 소비습관 분석 요약 응답")
public class UsageSummaryResponse {

    @Schema(description = "총 지출 금액(전체기간)", example = "199140")
    private int totalOutlay;

    @Schema(description = "총 수입 금액(전체기간)", example = "200000")
    private int totalIncome;

    @Schema(description = "가장 많은 지출 카테고리", example = "식품/건강")
    private String topOutlayCategory;

    @Schema(description = "가장 많은 수입 카테고리", example = "급여")
    private String topIncomeCategory;

    @Schema(description = "가장 적은 지출 카테고리")
    private String leastOutlayCategory;

    @Schema(description = "가장 적은 수입 카테고리")
    private String leastIncomeCategory;

    @Schema(description = "가장 많은 지출 상품명")
    private String mostOutlayItemName;

    @Schema(description = "가장 많은 수입 수입처")
    private String topIncomeImporter;

    @Schema(description = "가장 금액이 높은 지출 상품명")
    private String highestOutlayItemName;

    @Schema(description = "가장 금액이 높은 수입 수입처")
    private String highestIncomeImporter;
}

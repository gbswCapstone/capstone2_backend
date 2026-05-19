package Capstone.capstoneProject.dto.usage;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnalysisResponseDTO {
    private SummaryDTO summary;
    private CategoryDetailDTO topOutlayCategory;
    private CategoryDetailDTO leastOutlayCategory;
    private IncomeDetailDTO incomeDetail;
    private String advice;
    private String punchline;

    @Getter
    @Builder
    public static class SummaryDTO {
        private String outlay;
        private String income;
        private String analysis;
    }

    @Getter
    @Builder
    public static class CategoryDetailDTO {
        private String category;
        private String description;
    }

    @Getter
    @Builder
    public static class IncomeDetailDTO {
        private String importer;
        private String description;
    }
}

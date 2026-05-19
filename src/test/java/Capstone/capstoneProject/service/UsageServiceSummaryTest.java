package Capstone.capstoneProject.service;

import Capstone.capstoneProject.dto.Usages.CategorySummariesDTO;
import Capstone.capstoneProject.dto.Usages.UsageSummaryDTO;
import Capstone.capstoneProject.entity.UsageHistory;
import Capstone.capstoneProject.entity.Users.Users;
import Capstone.capstoneProject.enums.HistoryType;
import Capstone.capstoneProject.enums.UsageCategory;
import Capstone.capstoneProject.repository.UsageHistoryRepository;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UsageServiceSummaryTest {

    @Mock
    private AuthenticatedUserUtils authenticatedUserUtils;
    @Mock
    private UsageHistoryRepository usageHistoryRepository;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private BalanceService balanceService;

    @InjectMocks
    private UsageService usageService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(usageService, "aiServerUrl", "http://localhost:8080");
    }

    private UsageHistory buildHistory(UsageCategory category, HistoryType type, BigDecimal price, int amount) {
        return UsageHistory.builder()
                .category(category)
                .historyType(type)
                .price(price)
                .amount(amount)
                .name("테스트")
                .proDate(LocalDate.now())
                .users(Users.builder().id(1L).email("test@test.com").build())
                .build();
    }

    @Test
    @DisplayName("지출/수입 총계가 올바르게 집계됨 - enum 비교 버그 수정 검증")
    void totalOutlayAndIncomeAreCorrect() {
        List<UsageHistory> histories = List.of(
                buildHistory(UsageCategory.FOOD, HistoryType.OUTLAY, new BigDecimal("10000"), 1),
                buildHistory(UsageCategory.FOOD, HistoryType.OUTLAY, new BigDecimal("5000"), 1),
                buildHistory(UsageCategory.WAGE, HistoryType.INCOME, new BigDecimal("300000"), 1)
        );

        UsageSummaryDTO result = usageService.createUsageSummary(histories);

        assertThat(result.getTotalOutlay()).isEqualByComparingTo(new BigDecimal("15000"));
        assertThat(result.getTotalIncome()).isEqualByComparingTo(new BigDecimal("300000"));
    }

    @Test
    @DisplayName("카테고리별 첫 항목이 이중 집계되지 않음 - double-counting 버그 수정 검증")
    void firstItemInCategoryIsNotDoubleCounted() {
        List<UsageHistory> histories = List.of(
                buildHistory(UsageCategory.FOOD, HistoryType.OUTLAY, new BigDecimal("10000"), 1),
                buildHistory(UsageCategory.FOOD, HistoryType.OUTLAY, new BigDecimal("5000"), 1)
        );

        UsageSummaryDTO result = usageService.createUsageSummary(histories);

        CategorySummariesDTO foodSummary = result.getCategorySummaries().stream()
                .filter(c -> c.getCategory() == UsageCategory.FOOD)
                .findFirst()
                .orElseThrow();

        assertThat(foodSummary.getPrice()).isEqualByComparingTo(new BigDecimal("15000"));
        assertThat(foodSummary.getAmount()).isEqualTo(2);
    }

    @Test
    @DisplayName("단일 항목도 이중 집계되지 않음")
    void singleItemIsNotDoubleCounted() {
        List<UsageHistory> histories = List.of(
                buildHistory(UsageCategory.TRAFFIC, HistoryType.OUTLAY, new BigDecimal("1500"), 1)
        );

        UsageSummaryDTO result = usageService.createUsageSummary(histories);

        CategorySummariesDTO transportSummary = result.getCategorySummaries().stream()
                .filter(c -> c.getCategory() == UsageCategory.TRAFFIC)
                .findFirst()
                .orElseThrow();

        assertThat(transportSummary.getPrice()).isEqualByComparingTo(new BigDecimal("1500"));
        assertThat(transportSummary.getAmount()).isEqualTo(1);
    }

    @Test
    @DisplayName("빈 리스트 입력 시 0으로 초기화된 결과 반환")
    void emptyListReturnZeroTotals() {
        UsageSummaryDTO result = usageService.createUsageSummary(List.of());

        assertThat(result.getTotalOutlay()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(result.getTotalIncome()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(result.getCategorySummaries()).isEmpty();
    }

    @Test
    @DisplayName("여러 카테고리가 독립적으로 집계됨")
    void multipleCategoriesAreTrackedIndependently() {
        List<UsageHistory> histories = List.of(
                buildHistory(UsageCategory.FOOD, HistoryType.OUTLAY, new BigDecimal("10000"), 1),
                buildHistory(UsageCategory.TRAFFIC, HistoryType.OUTLAY, new BigDecimal("2000"), 1),
                buildHistory(UsageCategory.FOOD, HistoryType.OUTLAY, new BigDecimal("8000"), 1)
        );

        UsageSummaryDTO result = usageService.createUsageSummary(histories);

        assertThat(result.getCategorySummaries()).hasSize(2);

        BigDecimal foodTotal = result.getCategorySummaries().stream()
                .filter(c -> c.getCategory() == UsageCategory.FOOD)
                .map(CategorySummariesDTO::getPrice)
                .findFirst().orElseThrow();

        BigDecimal transportTotal = result.getCategorySummaries().stream()
                .filter(c -> c.getCategory() == UsageCategory.TRAFFIC)
                .map(CategorySummariesDTO::getPrice)
                .findFirst().orElseThrow();

        assertThat(foodTotal).isEqualByComparingTo(new BigDecimal("18000"));
        assertThat(transportTotal).isEqualByComparingTo(new BigDecimal("2000"));
    }
}

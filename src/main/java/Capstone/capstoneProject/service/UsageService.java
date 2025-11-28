package Capstone.capstoneProject.service;

import Capstone.capstoneProject.dto.Usages.IncomeRequest;
import Capstone.capstoneProject.dto.Usages.OutlayRequest;
import Capstone.capstoneProject.dto.Usages.UsageResponse;
import Capstone.capstoneProject.dto.Usages.UsageSearchTypeDTO;
import Capstone.capstoneProject.entity.UsageHistory;
import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.enums.HistoryType;
import Capstone.capstoneProject.repository.UsageHistoryRepository;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class UsageService {
    private final AuthenticatedUserUtils authenticatedUserUtils;
    private final UsageHistoryRepository usageHistoryRepository;
    private final RestTemplate restTemplate;


    public UsageResponse plusIncomeHistory(IncomeRequest request) {
        // user 정보 가져오기 (baarer token에서 추출)
        Users user = authenticatedUserUtils.getCurrentUser();

        // 외부 AI 서버로 importer 전송
        String url = "http://13.125.64.51:8080/income/income";
        Map<String, String> requestBody = Map.of("income_name", request.getImporter());
        ResponseEntity<Map> response = restTemplate.postForEntity(url, requestBody, Map.class);
        // 응답에서 category 추출
        String category = null;
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            category = (String) response.getBody().get("category");
        }
        // 값이 안 오면 null
        LocalDate proDate = (request.getProDate() != null)
                ? request.getProDate()
                : LocalDate.now();
        // 가격 계산 (가격 * 수량)
        BigDecimal price = request.getPrice().multiply(BigDecimal.valueOf(request.getAmount()));

        UsageHistory usageHistory = UsageHistory.builder()
                .users(user)
                .name(request.getImporter())
                .price(price)
                .proDate(proDate)
                .category(category)
                .amount(request.getAmount())
                .historyType(HistoryType.INCOME)
                .createdAt(LocalDateTime.now())
                .build();
        usageHistoryRepository.save(usageHistory);

        return new UsageResponse(usageHistory);
    }

    public UsageResponse plusOutlayHistory(OutlayRequest request) {
        // user 정보 가져오기 (baarer token에서 추출)
        Users user = authenticatedUserUtils.getCurrentUser();

        // 외부 AI 서버로 productName 전송
        String url = "http://13.125.64.51:8080/category/category";
        Map<String, String> requestBody = Map.of("product_name", request.getProductName());
        ResponseEntity<Map> response = restTemplate.postForEntity(url, requestBody, Map.class);
        // 응답에서 category 추출
        String category = null;
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            category = (String) response.getBody().get("category");
        }
        // 값이 안 오면 null
        LocalDate proDate = (request.getProDate() != null)
                ? request.getProDate()
                : LocalDate.now();
        // 가격 계산 (가격 * 수량)
        BigDecimal price = request.getPrice().multiply(BigDecimal.valueOf(request.getAmount()));

        UsageHistory usageHistory = UsageHistory.builder()
                .users(user)
                .name(request.getProductName())
                .price(price)
                .proDate(proDate)
                .category(category)
                .amount(request.getAmount())
                .historyType(HistoryType.OUTLAY)
                .createdAt(LocalDateTime.now())
                .build();
        usageHistoryRepository.save(usageHistory);

        return new UsageResponse(usageHistory);
    }

    public List<UsageResponse> getUsageList(UsageSearchTypeDTO typeDTO) {
        Users user = authenticatedUserUtils.getCurrentUser();

        // type 처리 (OUTLAY, IMCOME, ALL)
        HistoryType type = typeDTO.getType();
        if (type == HistoryType.ALL) type = null;

        LocalDate start = typeDTO.getStartDate();
        LocalDate end = typeDTO.getEndDate();

        // preset 처리 (today, thisWeek)
        if ("today".equalsIgnoreCase(typeDTO.getPreset())) {
            start = LocalDate.now();
            end = LocalDate.now();
        } else if ("thisWeek".equalsIgnoreCase(typeDTO.getPreset())) {
            LocalDate now = LocalDate.now();
            start = now.with(DayOfWeek.MONDAY);
            end = now.with(DayOfWeek.SUNDAY);
        }
        // year/month
        if (typeDTO.getYear() != null && typeDTO.getMonth() != null) {
            start = LocalDate.of(typeDTO.getYear(), typeDTO.getMonth(), 1);
            end = start.withDayOfMonth(start.lengthOfMonth());
        } else if (typeDTO.getYear() != null) {
            start = LocalDate.of(typeDTO.getYear(), 1, 1);
            end = LocalDate.of(typeDTO.getYear(), 12, 31);
        }

        // 기본값 보정
        if (start == null) start = LocalDate.of(2000,1,1);
        if (end == null) end = LocalDate.now();


        // 조회 (Repository가 LocalDate 파라미터 받아야함)
        List<UsageHistory> list = usageHistoryRepository.findByUserAndTypeDTO(user, type, start, end.plusDays(1));

        return list.stream().map(UsageResponse::new).toList();
    }





}

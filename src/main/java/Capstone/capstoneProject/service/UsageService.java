package Capstone.capstoneProject.service;

import Capstone.capstoneProject.dto.Usages.*;
import Capstone.capstoneProject.entity.UsageHistory;
import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.enums.HistoryType;
import Capstone.capstoneProject.enums.IncomeCategory;
import Capstone.capstoneProject.enums.OutlayCategory;
import Capstone.capstoneProject.enums.UsageSortType;
import Capstone.capstoneProject.exceptions.ConflictingSearchCriteriaException;
import Capstone.capstoneProject.exceptions.InvalidDateRangeException;
import Capstone.capstoneProject.exceptions.ReceiptAiServerException;
import Capstone.capstoneProject.exceptions.WeekNotInMonthException;
import Capstone.capstoneProject.repository.UsageHistoryRepository;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class UsageService {
    private final AuthenticatedUserUtils authenticatedUserUtils;
    private final UsageHistoryRepository usageHistoryRepository;
    private final RestTemplate restTemplate;


    public UsageResponse plusIncomeHistory(IncomeRequest request) {
        // user 정보 가져오기 (baarer token에서 추출)
        Users user = authenticatedUserUtils.getCurrentUser();

        // 외부 AI 서버로 importer 전송
        String url = "http://13.125.64.51:8080/income";
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
        String url = "http://13.125.64.51:8080/category";
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

    public List<UsageResponse> plusReceiptOutlay(ReceiptRequest request) {
        Users user = authenticatedUserUtils.getCurrentUser();

        String url = "http://13.125.64.51:8080/ocr/receipt";
        Map<String, String> requestBody = Map.of(
                "image_url", request.getImageUrl(),
                "filename", "receipt"
        );

        ResponseEntity<Map> response = restTemplate.postForEntity(url, requestBody, Map.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ReceiptAiServerException("영수증 AI 서버 호출 실패");
        }

        Map<String, Object> message = (Map<String, Object>) response.getBody().get("message");
        List<Map<String, Object>> dataList = (List<Map<String, Object>>) message.get("data");

        List<UsageResponse> usageList = new ArrayList<>();

        for (Map<String, Object> item : dataList) {
            BigDecimal price = new BigDecimal((String) item.get("price"));
            int amount = Integer.parseInt((String) item.get("quantity"));
            LocalDate proDate = LocalDate.parse((String) item.get("date"));
            String name = (String) item.get("productName");
            String category = (String) item.get("category");

            // 엔티티 저장
            UsageHistory usageHistory = UsageHistory.builder()
                    .users(user)
                    .name(name)
                    .price(price)
                    .proDate(proDate)
                    .category(category)
                    .amount(amount)
                    .historyType(HistoryType.OUTLAY)
                    .createdAt(LocalDateTime.now())
                    .build();

            UsageHistory savedHistory = usageHistoryRepository.save(usageHistory);

            // 저장된 엔티티 기반 DTO 변환
            UsageResponse usageResponse = new UsageResponse(savedHistory);

            usageList.add(usageResponse);
        }
        return usageList;
    }


    public List<UsageResponse> getUsageList(UsageSearchTypeDTO dto, UsageSortType usageSortType) {
        Users user = authenticatedUserUtils.getCurrentUser();

        boolean hasPreset = dto.getPresetType() != null;
        boolean hasCustomDates = dto.getStartDate() != null || dto.getEndDate() != null;

        boolean hasYMW = dto.getYear() != null || dto.getMonth() != null || dto.getWeek() != null;

        int activeGroupCount = 0;
        if (hasPreset) activeGroupCount++;
        if (hasCustomDates) activeGroupCount++;
        if (hasYMW) activeGroupCount++;

        // 검색 조건 그룹(Preset, 커스텀 날짜, 연/월/주) 중복 선택 시 예외 처리
        if (activeGroupCount > 1) {
            throw new ConflictingSearchCriteriaException("검색 조건(간편조회타입, (시작날짜, 끝날짜), 연/월/주)은 하나만 선택해야 합니다.");
        }

        HistoryType type = (dto.getType() == HistoryType.ALL) ? null : dto.getType();
        LocalDate start = null;
        LocalDate end = null;

        if (dto.getPresetType() != null) {
            switch (dto.getPresetType()) {
                case TODAY:
                    start = LocalDate.now();
                    end = LocalDate.now();
                    break;

                case THIS_WEEK:
                    LocalDate now = LocalDate.now();
                    start = now.with(DayOfWeek.MONDAY);
                    end = now.with(DayOfWeek.SUNDAY);
                    break;

                case THIS_MONTH:
                    LocalDate m = LocalDate.now();
                    start = m.withDayOfMonth(1);
                    end = m.withDayOfMonth(m.lengthOfMonth());
                    break;
            }
        }

        else if (dto.getYear() != null && dto.getMonth() != null && dto.getWeek() != null) {
            // 월별 주차 처리
            int year = dto.getYear();
            int month = dto.getMonth();
            int week = dto.getWeek();

            LocalDate monthStart = LocalDate.of(year, month, 1);
            int monthLength = monthStart.lengthOfMonth();

            int startDay = (week - 1) * 7 + 1;
            int endDay = week * 7;

            // 주차 예외처리
            if (startDay > monthLength) {
                throw new WeekNotInMonthException("해당 월(" + year + "년 " + month + "월)에는 요청한 주차(" + week + "주차)가 존재하지 않습니다.");
            }

            if (endDay > monthLength) {
                endDay = monthLength;
            }

            start = LocalDate.of(year, month, startDay);
            end = LocalDate.of(year, month, endDay);
        }

        else if (dto.getMonth() != null && dto.getWeek() != null) {
            int year = LocalDate.now().getYear(); // 현재 연도
            int month = dto.getMonth();
            int week = dto.getWeek();

            LocalDate monthStart = LocalDate.of(year, month, 1);
            int monthLength = monthStart.lengthOfMonth();

            int startDay = (week - 1) * 7 + 1;
            int endDay = week * 7;

            if (startDay > monthLength) {
                throw new WeekNotInMonthException(
                        "해당 월(" + year + "년 " + month + "월)에는 요청한 주차(" + week + "주차)가 존재하지 않습니다."
                );
            }

            if (endDay > monthLength) endDay = monthLength;

            start = LocalDate.of(year, month, startDay);
            end = LocalDate.of(year, month, endDay);
        }

        else if (dto.getWeek() != null) {

            int year = LocalDate.now().getYear();
            int month = LocalDate.now().getMonthValue();
            int week = dto.getWeek();


            LocalDate monthStart = LocalDate.of(year, month, 1);
            int monthLength = monthStart.lengthOfMonth();

            int startDay = (week - 1) * 7 + 1;
            int endDay = week * 7;

            if (startDay > monthLength) {
                throw new WeekNotInMonthException("이번 달(" + year + "년 " + month + "월)에는 요청한 주차(" + week + "주차)가 존재하지 않습니다.");
            }

            if (endDay > monthLength) {
                endDay = monthLength;
            }
            start = LocalDate.of(year, month, startDay);
            end = LocalDate.of(year, month, endDay);
        }

        else if (dto.getYear() != null && dto.getMonth() != null) {
            start = LocalDate.of(dto.getYear(), dto.getMonth(), 1);
            end = start.withDayOfMonth(start.lengthOfMonth());
        }
        else if (dto.getMonth() != null) {
            int currentYear = LocalDate.now().getYear();
            start = LocalDate.of(currentYear, dto.getMonth(), 1);
            end = start.withDayOfMonth(start.lengthOfMonth());
        }
        else if (dto.getYear() != null) {
            start = LocalDate.of(dto.getYear(), 1, 1);
            end = LocalDate.of(dto.getYear(), 12, 31);
        }
        else {
            start = dto.getStartDate();
            end = dto.getEndDate();

            // start만 오면 startDate 이후의 날짜로 처리
            if (start != null && end == null) {
                end = LocalDate.now();
            }
            // end만 오면 endDate 이전의 날짜로 처리
            else if (start == null && end != null) {
                start = LocalDate.of(2000, 1, 1); // 충분히 먼 과거
            }

            // 유효성 검사: 시작일이 종료일보다 늦으면 예외 처리
            if (start != null && end != null && start.isAfter(end)) {
                throw new InvalidDateRangeException("시작 날짜는 종료 날짜보다 늦을 수 없습니다.");
            }
        }

        // 아무 값도 없으면 최근 1개월 조회
        if (start == null && end == null) {
            end = LocalDate.now();
            start = end.minusMonths(1);
        }

        else {
            if (start == null) start = LocalDate.of(2000, 1, 1);
            if (end == null) end = LocalDate.now();
        }
        if (usageSortType == null) {
            usageSortType = UsageSortType.RECENT;
        }
        List<UsageHistory> list = usageHistoryRepository.searchDynamic(
                user.getId(),
                type,
                start,
                end,
                usageSortType
        );

        return list.stream().map(UsageResponse::new).toList();
    }

    public UsageSummaryResponse getUsageSummary() {
        Users user = authenticatedUserUtils.getCurrentUser();
        Pageable pageable = PageRequest.of(0, 1);
        // 총지출, 총수입 가격
        Integer totalOutlay = usageHistoryRepository.sumOutlayByUser(user.getId(), pageable).get(0);
        Integer totalIncome = usageHistoryRepository.sumIncomeByUser(user.getId(), pageable).get(0);

        // 가장 수량 많은 카테고리
        String topOutlayCategory = usageHistoryRepository.findTopCategoryByOutlay(user.getId(), pageable).get(0);
        String topIncomeCategory = usageHistoryRepository.findLeastCategoryByOutlay(user.getId(), pageable).get(0);

        // 가장 수량 적은 카테고리
        String leastOutlayCategory = findLeastOutlayCategory(user.getId());
        String leastIncomeCategory = findLeastIncomeCategory(user.getId());
//        String leastOutlayCategory = usageHistoryRepository.findTopCategoryByIncome(user.getId(), pageable).get(0);
//        String leastIncomeCategory = usageHistoryRepository.findLeastCategoryByIncome(user.getId(), pageable).get(0);
        
        // 가장 많은 지출&수입 이름(수량기준)
        String mostOutlayItemName = usageHistoryRepository.findMostOutlayItemName(user.getId(), pageable).get(0);
        String topIncomeImporter = usageHistoryRepository.findtopIncomeImporter(user.getId(), pageable).get(0);
        
        // 가장 비싼 지출&수입 이름(가격기준)
        String highestOutlayItemName = usageHistoryRepository.findHighestOutlayItemName(user.getId(), pageable).get(0);
        String highestIncomeImporter = usageHistoryRepository.findhighestIncomeImporter(user.getId(), pageable).get(0);
        
        return UsageSummaryResponse.builder()
                .totalOutlay(totalOutlay)
                .totalIncome(totalIncome)
                .topOutlayCategory(topOutlayCategory)
                .topIncomeCategory(topIncomeCategory)
                .leastOutlayCategory(leastOutlayCategory)
                .leastIncomeCategory(leastIncomeCategory)
                .mostOutlayItemName(mostOutlayItemName)
                .topIncomeImporter(topIncomeImporter)
                .highestOutlayItemName(highestOutlayItemName)
                .highestIncomeImporter(highestIncomeImporter)
                .build();
    }

    public String findLeastOutlayCategory(Long userId) {

        // DB에서 실제 등장한 카테고리 count 가져오기
        List<Object[]> results = usageHistoryRepository.countOutlayCategory(userId);

        // Map<카테고리, 카운트>
        Map<OutlayCategory, Long> countMap = new HashMap<>();

        // DB 결과 넣기
        for (Object[] row : results) {
            OutlayCategory category = (OutlayCategory) row[0];
            Long count = (Long) row[1];
            countMap.put(category, count);
        }

        OutlayCategory leastCategory = null;
        Long minCount = Long.MAX_VALUE;

        for (OutlayCategory category : OutlayCategory.values()) {
            Long count = countMap.getOrDefault(category, 0L);
            if (count < minCount) {
                minCount = count;
                leastCategory = category;
            }
        }

        return leastCategory.name();
    }

    public String findLeastIncomeCategory(Long userId) {

        List<Object[]> results = usageHistoryRepository.countIncomeCategory(userId);

        Map<IncomeCategory, Long> countMap = new HashMap<>();

        for (Object[] row : results) {
            IncomeCategory category = (IncomeCategory) row[0];
            Long count = (Long) row[1];
            countMap.put(category, count);
        }

        IncomeCategory leastCategory = null;
        Long minCount = Long.MAX_VALUE;

        for (IncomeCategory category : IncomeCategory.values()) {
            Long count = countMap.getOrDefault(category, 0L);
            if (count < minCount) {
                minCount = count;
                leastCategory = category;
            }
        }

        return leastCategory.name();
    }








}

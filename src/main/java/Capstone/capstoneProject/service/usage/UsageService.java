package Capstone.capstoneProject.service.usage;

import Capstone.capstoneProject.dto.usage.*;
import Capstone.capstoneProject.entity.usage.UsageHistory;
import Capstone.capstoneProject.entity.user.Users;
import Capstone.capstoneProject.enums.*;
import Capstone.capstoneProject.exceptions.badGateway.ChatBotMessageFailedException;
import Capstone.capstoneProject.exceptions.badRequest.ConflictingSearchCriteriaException;
import Capstone.capstoneProject.exceptions.badRequest.InvalidDateRangeException;
import Capstone.capstoneProject.exceptions.badRequest.InvalidQuantityException;
import Capstone.capstoneProject.exceptions.badRequest.WeekNotInMonthException;
import Capstone.capstoneProject.exceptions.notfound.UsageHistoryNotFoundException;
import Capstone.capstoneProject.exceptions.serverError.ReceiptAiServerException;
import Capstone.capstoneProject.repository.usage.UsageHistoryRepository;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UsageService {
    private final AuthenticatedUserUtils authenticatedUserUtils;
    private final UsageHistoryRepository usageHistoryRepository;
    private final RestTemplate restTemplate;
    private final BalanceService balanceService;

    @Value("${ai.server.url:http://13.125.64.51:8080}")
    private String aiServerUrl;


    public UsageResponse plusIncomeHistory(IncomeRequest request) {
        // user 정보 가져오기 (baarer token에서 추출)
        Users user = authenticatedUserUtils.getCurrentUser();
        if (request.getAmount()<=0) {
            throw new InvalidQuantityException("수량은 최소 1개 이상이어야 합니다.");
        }

        // 외부 AI 서버로 importer 전송
        String url = aiServerUrl + "/income";
        Map<String, String> requestBody = Map.of("income_name", request.getImporter());
        ResponseEntity<Map> response = restTemplate.postForEntity(url, requestBody, Map.class);
        // 응답에서 category 추출
        String categoryStr = "";
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            categoryStr = (String) response.getBody().get("category");
        }
        UsageCategory category = UsageCategory.valueOf(categoryStr);
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

        // 잔액 변경
        balanceService.applyUsage(user, usageHistory);
        return new UsageResponse(usageHistory);
    }

    public UsageResponse plusOutlayHistory(OutlayRequest request) {
        // user 정보 가져오기 (baarer token에서 추출)
        Users user = authenticatedUserUtils.getCurrentUser();
        if (request.getAmount()<=0) {
            throw new InvalidQuantityException("수량은 최소 1개 이상이어야 합니다.");
        }

        // 외부 AI 서버로 productName 전송
        String url = aiServerUrl + "/category";
        Map<String, String> requestBody = Map.of("product_name", request.getProductName());
        ResponseEntity<Map> response = restTemplate.postForEntity(url, requestBody, Map.class);
        // 응답에서 category 추출
        String categoryStr = "";
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            categoryStr = (String) response.getBody().get("category");
        }
        UsageCategory category = UsageCategory.valueOf(categoryStr);
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

        // 잔액 변경
        balanceService.applyUsage(user, usageHistory);
        return new UsageResponse(usageHistory);
    }

    public ReceiptResponse plusReceiptOutlay(MultipartFile image) {
        Users user = authenticatedUserUtils.getCurrentUser();
        String url = aiServerUrl + "/ocr";

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        try {
            body.add("image", new ByteArrayResource(image.getBytes()) {
                @Override
                public String getFilename() {
                    return image.getOriginalFilename();
                }
            });
        } catch (IOException e) {
            throw new ReceiptAiServerException("이미지 변환 중 오류가 발생했습니다.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ReceiptAiServerException("영수증 AI 서버 호출 실패");
        }

        Map<String, Object> responseBody = response.getBody();

        List<Map<String, Object>> dataList = (List<Map<String, Object>>) responseBody.get("message");

        if (dataList == null || dataList.isEmpty()) {
            throw new ReceiptAiServerException("추출된 영수증 데이터가 없습니다.");
        }

        List<ReceiptListDTO> usageList = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;
        LocalDate receiptDate = null;

        for (Map<String, Object> item : dataList) {

            BigDecimal price = new BigDecimal(String.valueOf(item.get("price")));
            int amount = Integer.parseInt(String.valueOf(item.get("quantity")));
            LocalDate proDate = LocalDate.parse(String.valueOf(item.get("date")));
            String name = (String) item.get("productName");

            UsageCategory category = UsageCategory.valueOf(String.valueOf(item.get("category")));

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
            usageList.add(ReceiptListDTO.from(savedHistory));

            // 잔액 변경
            balanceService.applyUsage(user, savedHistory);

            totalPrice = totalPrice.add(price.multiply(BigDecimal.valueOf(amount)));
            receiptDate = proDate;
        }



        return ReceiptResponse.builder()
                .usageResponseList(usageList)
                .totalPrice(totalPrice)
                .proDate(receiptDate)
                .build();
    }

    public ReceiptResponse patchReceiptOutlay(ReceiptPatchRequest request) {
        Users user = authenticatedUserUtils.getCurrentUser();


        List<ReceiptListDTO> resultList = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;
        LocalDate proDate = null;

        for (ReceiptItemPatchDTO item : request.getItems()) {

            UsageHistory usageHistory = usageHistoryRepository
                    .findByIdAndUsers(item.getUsageHistoryId(), user)
                    .orElseThrow(() -> new UsageHistoryNotFoundException("해당 지출 내역이 존재하지 않습니다."));

            // 수정 전 금액
            BigDecimal beforeTotal =
                    usageHistory.getPrice()
                            .multiply(BigDecimal.valueOf(usageHistory.getAmount()));

            // id빼고 null처리 id는 필수 입력
            String name = (item.getName() != null) ? item.getName() : usageHistory.getName();
            BigDecimal price = (item.getPrice() != null) ? item.getPrice() : usageHistory.getPrice();
            UsageCategory category = (item.getCategory() != null) ? item.getCategory() : usageHistory.getCategory();
            Integer amount = (item.getAmount() != null) ? item.getAmount() : usageHistory.getAmount();

            usageHistory.update(
                    name,
                    price,
                    category,
                    amount
            );

            UsageHistory saved = usageHistoryRepository.save(usageHistory);

            // 수정 후 금액
            BigDecimal afterTotal =
                    price.multiply(BigDecimal.valueOf(amount));

            // 차액 계산
            BigDecimal diff = afterTotal.subtract(beforeTotal);

            // 잔액 변경
            if (diff.compareTo(BigDecimal.ZERO) != 0) {
                UsageHistory diffHistory = UsageHistory.builder()
                        .users(user)
                        .price(diff)
                        .historyType(HistoryType.OUTLAY)
                        .build();

                balanceService.applyUsage(user, diffHistory);
            }

            resultList.add(ReceiptListDTO.from(saved));
            totalPrice = totalPrice.add(
                    saved.getPrice().multiply(BigDecimal.valueOf(saved.getAmount()))
            );
            proDate = saved.getProDate();
        }

        return ReceiptResponse.builder()
                .usageResponseList(resultList)
                .totalPrice(totalPrice)
                .proDate(proDate)
                .build();
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

        HistoryType type = dto.getType();
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

        // 아무 값도 없으면 전체 조회
        if (start == null && end == null) {
            List<UsageHistory> usages = usageHistoryRepository.findAllByUsers(user);
            return usages.stream().map(UsageResponse::new).toList();
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

        // 총 지출, 총수입 가격
        Integer totalOutlay = usageHistoryRepository.sumOutlayByUser(user.getId(), pageable)
                .stream()
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(0);

        Integer totalIncome = usageHistoryRepository.sumIncomeByUser(user.getId(), pageable)
                .stream()
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(0);

        // 가장 많은 지출, 수입 카테고리
        UsageCategory topOutlayCategory = usageHistoryRepository.findTopCategoryByOutlay(user.getId(), pageable)
                .stream()
                .findFirst()
                .orElse(null);

        UsageCategory topIncomeCategory = usageHistoryRepository.findTopCategoryByIncome(user.getId(), pageable)
                .stream()
                .findFirst()
                .orElse(null);


        // 가장 적은 지출 카테고리
        UsageCategory leastOutlayCategory = findLeastOutlayCategory(user.getId());
        // 가장 적은 수입 카테고리
        UsageCategory leastIncomeCategory = findLeastIncomeCategory(user.getId());

        // 가장 많은 지출&수입 이름(수량기준)
        String mostOutlayItemName = usageHistoryRepository.findMostOutlayItemName(user.getId(), pageable)
                .stream()
                .findFirst()
                .orElse(null);

        String topIncomeImporter = usageHistoryRepository.findtopIncomeImporter(user.getId(), pageable)
                .stream()
                .findFirst()
                .orElse(null);

        // 가장 비싼 지출&수입 이름(가격기준)
        String highestOutlayItemName = usageHistoryRepository.findHighestOutlayItemName(user.getId(), pageable)
                .stream()
                .findFirst()
                .orElse(null);

        String highestIncomeImporter = usageHistoryRepository.findhighestIncomeImporter(user.getId(), pageable)
                .stream()
                .findFirst()
                .orElse(null);

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

    public AnalysisResponseDTO getAiUsageAnalysis() {
        UsageSummaryResponse request = getUsageSummary();
        String url = aiServerUrl + "/analysis";
        // 요청 그대로 전달
        ResponseEntity<AnalysisResponseDTO> response =
                restTemplate.postForEntity(
                        url, request, AnalysisResponseDTO.class
                );
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ChatBotMessageFailedException("챗봇 메시지 생성에 실패했습니다.");
        }
        return response.getBody();
    }

    public UsageCategory findLeastOutlayCategory(Long userId) {

        // DB에서 실제 등장한 카테고리 count 가져오기
        List<Object[]> results = usageHistoryRepository.countOutlayCategory(userId);

        // Map<카테고리, 카운트>
        Map<UsageCategory, Long> countMap = new HashMap<>();

        // DB 결과 넣기
        for (Object[] row : results) {
            UsageCategory category = (UsageCategory) row[0];
            Long count = (Long) row[1];
            countMap.put(category, count);
        }

        UsageCategory leastCategory = null;
        Long minCount = Long.MAX_VALUE;

        for (UsageCategory category : UsageCategory.values()) {
            Long count = countMap.getOrDefault(category, 0L);
            if (count < minCount) {
                minCount = count;
                leastCategory = category;
            }
        }

        return leastCategory;
    }

    public UsageCategory findLeastIncomeCategory(Long userId) {

        List<Object[]> results = usageHistoryRepository.countIncomeCategory(userId);

        Map<UsageCategory, Long> countMap = new HashMap<>();

        for (Object[] row : results) {
            UsageCategory category = (UsageCategory) row[0];
            Long count = (Long) row[1];
            countMap.put(category, count);
        }

        UsageCategory leastCategory = null;
        Long minCount = Long.MAX_VALUE;

        for (UsageCategory category : UsageCategory.values()) {
            Long count = countMap.getOrDefault(category, 0L);
            if (count < minCount) {
                minCount = count;
                leastCategory = category;
            }
        }

        return leastCategory;
    }

    public UsageSummaryDTO createUsageSummary(List<UsageHistory> currentMonthHistories) {

        if (currentMonthHistories == null || currentMonthHistories.isEmpty()) {
            return UsageSummaryDTO.builder()
                    .month(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")))
                    .totalOutlay(BigDecimal.ZERO)
                    .totalIncome(BigDecimal.ZERO)
                    .categorySummaries(Collections.emptyList())
                    .build();
        }

        BigDecimal totalOutlay = BigDecimal.ZERO;
        BigDecimal totalIncome = BigDecimal.ZERO;

        Map<UsageCategory, CategorySummariesDTO> categoryMap = new HashMap<>();

        for (UsageHistory history : currentMonthHistories) {

            BigDecimal price = history.getPrice(); // 사용 금액
            UsageCategory category = history.getCategory();

            // 전체 합계
            if (history.getHistoryType() == HistoryType.OUTLAY) {
                totalOutlay = totalOutlay.add(price);
            } else if (history.getHistoryType() == HistoryType.INCOME) {
                totalIncome = totalIncome.add(price);
            }

            // 카테고리별 요약
            CategorySummariesDTO summary = categoryMap.computeIfAbsent(
                    category,
                    k -> new CategorySummariesDTO(category, BigDecimal.ZERO, 0)
            );

            summary.setPrice(summary.getPrice().add(price));
            summary.setAmount(summary.getAmount() + 1);
        }

        return UsageSummaryDTO.builder()
                .month(YearMonth.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM")))
                .totalOutlay(totalOutlay)
                .totalIncome(totalIncome)
                .categorySummaries(new ArrayList<>(categoryMap.values()))
                .build();
    }


}

package Capstone.capstoneProject.service;

import Capstone.capstoneProject.dto.IncomeRequest;
import Capstone.capstoneProject.dto.OutlayRequest;
import Capstone.capstoneProject.dto.UsageResponse;
import Capstone.capstoneProject.entity.UsageHistory;
import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.enums.HistoryType;
import Capstone.capstoneProject.repository.UsageHistoryRepository;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
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
        String url = "http://54.180.227.15/income";
        Map<String, String> requestBody = Map.of("income_name", request.getImporter());
        ResponseEntity<Map> response = restTemplate.postForEntity(url, requestBody, Map.class);
        // 응답에서 category 추출
        String category = null;
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            category = (String) response.getBody().get("category");
        }
        // 값이 안 오면 null
        LocalDateTime proDate = (request.getProDate() != null)
                ? request.getProDate()
                : LocalDateTime.now();

        UsageHistory usageHistory = UsageHistory.builder()
                .users(user)
                .name(request.getImporter())
                .price(request.getPrice())
                .proDate(proDate)
                .category(category)
                .historyType(HistoryType.INCOME)
                .build();
        usageHistoryRepository.save(usageHistory);

        return new UsageResponse(usageHistory);
    }

    public UsageResponse plusOutlayHistory(OutlayRequest request) {
        // user 정보 가져오기 (baarer token에서 추출)
        Users user = authenticatedUserUtils.getCurrentUser();

        // 외부 AI 서버로 importer 전송
        String url = "http://54.180.227.15/category";
        Map<String, String> requestBody = Map.of("product_name", request.getProductName());
        ResponseEntity<Map> response = restTemplate.postForEntity(url, requestBody, Map.class);
        // 응답에서 category 추출
        String category = null;
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            category = (String) response.getBody().get("category");
        }
        // 값이 안 오면 null
        LocalDateTime proDate = (request.getProDate() != null)
                ? request.getProDate()
                : LocalDateTime.now();

        UsageHistory usageHistory = UsageHistory.builder()
                .users(user)
                .name(request.getProductName())
                .price(request.getPrice())
                .proDate(proDate)
                .category(category)
                .historyType(HistoryType.OUTLAY)
                .build();
        usageHistoryRepository.save(usageHistory);

        return new UsageResponse(usageHistory);
    }




}

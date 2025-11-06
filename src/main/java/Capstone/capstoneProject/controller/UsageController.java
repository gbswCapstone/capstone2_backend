package Capstone.capstoneProject.controller;


import Capstone.capstoneProject.dto.IncomeRequest;
import Capstone.capstoneProject.dto.OutlayRequest;
import Capstone.capstoneProject.dto.UsageResponse;
import Capstone.capstoneProject.global.ApiResponse;
import Capstone.capstoneProject.service.UsageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usages")
@RequiredArgsConstructor
public class UsageController {
    private final UsageService usageService;

    @PostMapping("/income")
    @Operation(summary = "사용내역 수입 추가", description = "사용내역 수입 추가 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<UsageResponse>> plusIncome(@RequestBody IncomeRequest request) {
        UsageResponse result = usageService.plusIncomeHistory(request);
        return ResponseEntity.ok(ApiResponse.ok(result, "추가되었습니다."));
    }

    @PostMapping("/outlay")
    @Operation(summary = "사용내역 지출 추가", description = "사용내역 지출 추가 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<UsageResponse>> plusOutlay(@RequestBody OutlayRequest request) {
        UsageResponse result = usageService.plusOutlayHistory(request);
        return ResponseEntity.ok(ApiResponse.ok(result, "추가되었습니다."));
    }




}

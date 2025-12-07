package Capstone.capstoneProject.controller;

import Capstone.capstoneProject.dto.Usages.*;
import Capstone.capstoneProject.enums.UsageSortType;
import Capstone.capstoneProject.global.ApiResponse;
import Capstone.capstoneProject.service.UsageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usages")
@RequiredArgsConstructor
public class UsageController {
    private final UsageService usageService;

    @PostMapping("/income")
    @Operation(summary = "사용내역 수입 추가", description = "사용내역 수입 추가 시 사용하는 API 입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401", description = "로그인이 필요합니다.",
            content = @Content)
    })
    public ResponseEntity<ApiResponse<UsageResponse>> plusIncome(@RequestBody IncomeRequest request) {
        UsageResponse result = usageService.plusIncomeHistory(request);
        return ResponseEntity.ok(ApiResponse.ok(result, "추가되었습니다."));
    }

    @PostMapping("/outlay")
    @Operation(summary = "사용내역 지출 추가", description = "사용내역 지출 추가 시 사용하는 API 입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401", description = "로그인이 필요합니다.",
            content = @Content)

    })
    public ResponseEntity<ApiResponse<UsageResponse>> plusOutlay(@RequestBody OutlayRequest request) {
        UsageResponse result = usageService.plusOutlayHistory(request);
        return ResponseEntity.ok(ApiResponse.ok(result, "추가되었습니다."));
    }

    @PostMapping("/receipt")
    @Operation(summary = "영수증 지출 추가", description = "영수증 지출 추가 시 사용하는 API 입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500", description = "AI 서버 호출에 실패했습니다.",
            content = @Content
            ),  @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401", description = "로그인이 필요합니다.",
            content = @Content)
    })
    public ResponseEntity<ApiResponse<List<UsageResponse>>> plusReceiptOutlay
            (@RequestBody ReceiptRequest request) {
        List<UsageResponse> result = usageService.plusReceiptOutlay(request);
        return ResponseEntity.ok(ApiResponse.ok(result, "추가되었습니다."));
    }


    @GetMapping
    @Operation(summary = "사용내역 조회", description = "사용내역 조회 시 사용하는 API 입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400", description = "해당 월에 요청하는 주차가 없습니다.",
            content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "검색 조건(간편조회타입, (시작날짜, 끝날짜), 연/월/주)은 하나만 선택해야 합니다.",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401", description = "로그인이 필요합니다.",
            content = @Content)
    })
    public ResponseEntity<ApiResponse<List<UsageResponse>>> getListUsage(
            @ParameterObject @Valid @ModelAttribute UsageSearchTypeDTO typeDTO,
            @Parameter(description = "정렬 방식 (RECENT : 최신순, OLDEST : 오래된순, LOW_PRICE : 가격 낮은순, HIGH_PRICE : 가격 높은순")
            @RequestParam(required = false) UsageSortType usageSortType) {
            List<UsageResponse> result = usageService.getUsageList(typeDTO, usageSortType);
            return ResponseEntity.ok(ApiResponse.ok(result, "조회되었습니다."));
    }

}

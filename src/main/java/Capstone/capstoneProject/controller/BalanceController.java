package Capstone.capstoneProject.controller;


import Capstone.capstoneProject.dto.BalanceDTO;
import Capstone.capstoneProject.global.ApiResponse;
import Capstone.capstoneProject.service.BalanceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/balances")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;

    @PostMapping
    @Operation(summary = "유저 잔액 등록", description = "유저 잔액 등록 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<BalanceDTO>> createBalance(@RequestBody BalanceDTO request) {
        BalanceDTO result = balanceService.createBalance(request);
        return ResponseEntity.ok(ApiResponse.ok(result, "등록되었습니다."));
    }

    @GetMapping
    @Operation(summary = "유저 잔액 조회", description = "유저 잔액 조회 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<BalanceDTO>> getBalance() {
        BalanceDTO result = balanceService.getBalance();
        return ResponseEntity.ok(ApiResponse.ok(result, "조회되었습니다."));
    }

    @PutMapping
    @Operation(summary = "유저 잔액 수정", description = "유저 잔액 수정 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<BalanceDTO>> patchBalance
            (@PathVariable Long id, @RequestBody BalanceDTO request) {
        BalanceDTO result = balanceService.patchBalance(request);
        return ResponseEntity.ok(ApiResponse.ok(result, "수정되었습니다."));
    }
}

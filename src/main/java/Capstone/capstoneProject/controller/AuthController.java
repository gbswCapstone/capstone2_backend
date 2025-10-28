package Capstone.capstoneProject.controller;

import Capstone.capstoneProject.dto.*;
import Capstone.capstoneProject.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "로그인", description = "일반 로그인 시 사용하는 API 입니다.")
    @PostMapping("api/auth/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody LoginRequest request) {
        TokenResponse tokenResponse = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ok(tokenResponse));
    }

    @Operation(summary = "자동로그인", description = "자동 로그인 시 사용하는 API 입니다. (body에 refreshToken 넣어야함)")
    @PostMapping("api/auth/auto-login")
    public ResponseEntity<ApiResponse<TokenResponse>> autoLogin(@RequestBody AutoLoginRequest request) {
        TokenResponse tokenResponse = authService.autoLogin(request);
        return ResponseEntity.ok(ApiResponse.ok(tokenResponse));
    }

    @Operation(summary = "구글 로그인", description = "request값없음, response값 일반로그인이랑 똑같음")
    @GetMapping("/login/oauth2/code/google")
    public String googleLoginDoc() {
        return "Swagger 표시용 엔드포인트입니다. 실제로는 Spring Security가 처리합니다.";
    }

    @Operation(summary = "카카오 로그인", description = "request값없음, response값 일반로그인이랑 똑같음")
    @GetMapping("/login/oauth2/code/kakao")
    public String kakaoLoginDoc() {
        return "Swagger 표시용 엔드포인트입니다. 실제로는 Spring Security가 처리합니다.";
    }



    // 구글로그인 http://localhost:8004/login/oauth2/code/google
    // 카카오로그인 http://localhost:8004/login/oauth2/code/kakao





}

package Capstone.capstoneProject.controller;

import Capstone.capstoneProject.dto.ApiResponse;
import Capstone.capstoneProject.dto.LoginRequest;
import Capstone.capstoneProject.dto.SignupRequest;
import Capstone.capstoneProject.dto.TokenResponse;
import Capstone.capstoneProject.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입", description = "회원가입 시 사용하는 API 입니다.")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Object>> signup(@RequestBody SignupRequest request) {
        authService.signup(request);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @Operation(summary = "로그인", description = "일반 로그인 시 사용하는 API 입니다.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody LoginRequest request) {
        TokenResponse tokenResponse = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ok(tokenResponse));
    }


    // 구글로그인 http://localhost:8004/login/oauth2/code/google
    // 카카오로그인 http://localhost:8004/login/oauth2/code/kakao





}

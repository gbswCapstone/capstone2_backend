package Capstone.capstoneProject.controller;

import Capstone.capstoneProject.dto.*;
import Capstone.capstoneProject.global.ApiResponse;
import Capstone.capstoneProject.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401", description = "비밀번호가 일치하지 않습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404", description = "계정이 존재하지 않습니다."
            )
    })
    public ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody LoginRequest request) {
        TokenResponse tokenResponse = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ok(tokenResponse));
    }


    @Operation(summary = "자동로그인", description = "자동 로그인 시 사용하는 API 입니다. (body에 refreshToken 넣어야함)")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403", description = "유효하지 않은 리프레시 토큰 입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404", description = "계정이 존재하지 않습니다."
            )
    })
    @PostMapping("api/auth/auto-login")
    public ResponseEntity<ApiResponse<TokenResponse>> autoLogin(@RequestBody AutoLoginRequest request) {
        TokenResponse tokenResponse = authService.autoLogin(request);
        return ResponseEntity.ok(ApiResponse.ok(tokenResponse));
    }

    @PostMapping("api/auth/log-out")
    @Operation(summary = "로그아웃", description = "클라이언트에서 토큰 삭제&로그인화면처리")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401", description = "로그인이 필요합니다.")
    })
    public ResponseEntity<ApiResponse<Void>> logout() {
        authService.logOut();
        return ResponseEntity.ok(ApiResponse.ok("로그아웃 되었습니다."));
    }

    @Operation(summary = "구글 로그인", description = "구글 로그인 시 사용하는 API 입니다.")
    @PostMapping("api/auth/login/google")
    public ResponseEntity<ApiResponse<TokenResponse>> googleLogin(@RequestBody OauthRequest request) {
        TokenResponse tokenResponse = authService.googleLogin(request);
        return ResponseEntity.ok(ApiResponse.ok(tokenResponse));
    }

    @Operation(summary = "카카오 로그인", description = "카카오 로그인 시 사용하는 API 입니다.")
    @PostMapping("api/auth/login/kakao")
    public ResponseEntity<ApiResponse<TokenResponse>> kakaoLogin(@RequestBody OauthRequest request) {
        TokenResponse tokenResponse = authService.kakaoLogin(request);
        return ResponseEntity.ok(ApiResponse.ok(tokenResponse));
    }
}

package Capstone.capstoneProject.security.oauth;

import Capstone.capstoneProject.global.ApiResponse;
import Capstone.capstoneProject.dto.TokenResponse;
import Capstone.capstoneProject.entity.AuthToken;
import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.exceptions.notfound.UserNotFoundException;
import Capstone.capstoneProject.repository.AuthTokenRepository;
import Capstone.capstoneProject.repository.UserRepository;
import Capstone.capstoneProject.security.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

// oauth로그인에 성공하면 토큰을 내려줌
@Component
@RequiredArgsConstructor

public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final AuthTokenRepository authTokenRepository;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        // 인증된 사용자 정보 꺼내기
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        // provider 판별 (Google / Kakao)
        String provider = getProviderName(oauthUser);

        // provider별 이메일 추출
        String email = extractEmail(provider, oauthUser);

        if (email == null) {
            throw new UserNotFoundException(provider + " 로그인에서 이메일을 가져올 수 없습니다. (카카오 개발자 콘솔의 동의 항목 확인)");
        }

        // 사용자 조회
        Users user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));

        // JWT 발급
        String accessToken = jwtTokenProvider.createToken(user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

        // 기존 refresh token 삭제 후 새로 저장
        authTokenRepository.deleteByUser(user);
        AuthToken tokenEntity = AuthToken.builder()
                .user(user)
                .refreshToken(refreshToken)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .createdAt(LocalDateTime.now())
                .build();
        authTokenRepository.save(tokenEntity);

        // 응답 생성
        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken);
        ApiResponse<TokenResponse> apiResponse = ApiResponse.ok(tokenResponse);

        // JSON 응답 전송
        response.setContentType("application/json;charset=UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        response.getWriter().flush();
    }

    /**
     * provider 판별 메서드
     *  - Google: attributes에 "sub" 키가 존재
     *  - Kakao: attributes에 "id" 키가 존재
     */
    private String getProviderName(OAuth2User oauthUser) {
        Map<String, Object> attributes = oauthUser.getAttributes();
        if (attributes.containsKey("sub")) return "google";
        if (attributes.containsKey("id")) return "kakao";
        return "unknown";
    }

    /**
     *  provider별 이메일 추출 메서드
     *  - Google: email이 최상위
     *  - Kakao: kakao_account.email 내부
     */
    private String extractEmail(String provider, OAuth2User oauthUser) {
        if ("google".equals(provider)) {
            return (String) oauthUser.getAttribute("email");
        } else if ("kakao".equals(provider)) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) oauthUser.getAttribute("kakao_account");
            if (kakaoAccount != null) {
                return (String) kakaoAccount.get("email");
            }
        }
        return null;
    }
}



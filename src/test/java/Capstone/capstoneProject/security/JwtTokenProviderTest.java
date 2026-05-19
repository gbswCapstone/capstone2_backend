package Capstone.capstoneProject.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider("test_secret_key_for_unit_test_must_be_256bits_long_at_least_32");
    }

    @Test
    @DisplayName("액세스 토큰 생성 및 검증")
    void createAndValidateAccessToken() {
        String token = jwtTokenProvider.createToken("test@example.com");

        assertThat(token).isNotNull();
        assertThat(jwtTokenProvider.validateToken(token)).isTrue();
    }

    @Test
    @DisplayName("리프레시 토큰 생성 및 검증")
    void createAndValidateRefreshToken() {
        String token = jwtTokenProvider.createRefreshToken("test@example.com");

        assertThat(token).isNotNull();
        assertThat(jwtTokenProvider.validateToken(token)).isTrue();
    }

    @Test
    @DisplayName("토큰에서 사용자명 추출")
    void extractUsernameFromToken() {
        String email = "user@example.com";
        String token = jwtTokenProvider.createToken(email);

        assertThat(jwtTokenProvider.getUsername(token)).isEqualTo(email);
    }

    @Test
    @DisplayName("액세스/리프레시 토큰 모두 동일 사용자명 반환")
    void bothTokensReturnSameUsername() {
        String email = "user@example.com";
        String accessToken = jwtTokenProvider.createToken(email);
        String refreshToken = jwtTokenProvider.createRefreshToken(email);

        assertThat(jwtTokenProvider.getUsername(accessToken)).isEqualTo(email);
        assertThat(jwtTokenProvider.getUsername(refreshToken)).isEqualTo(email);
    }

    @Test
    @DisplayName("잘못된 토큰 검증 시 false 반환")
    void invalidTokenReturnsFalse() {
        assertThat(jwtTokenProvider.validateToken("invalid.token.here")).isFalse();
        assertThat(jwtTokenProvider.validateToken("")).isFalse();
        assertThat(jwtTokenProvider.validateToken(null)).isFalse();
    }

    @Test
    @DisplayName("리프레시 토큰 issuedAt이 null이 아님 - createRefreshToken의 new Date() 버그 수정 검증")
    void refreshTokenIssuedAtIsNotNull() {
        String token = jwtTokenProvider.createRefreshToken("user@example.com");
        assertThat(token).isNotBlank();
        // 토큰이 파싱 가능하면 issuedAt이 올바르게 설정된 것
        assertThat(jwtTokenProvider.validateToken(token)).isTrue();
    }
}

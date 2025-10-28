package Capstone.capstoneProject.service;

import Capstone.capstoneProject.dto.*;
import Capstone.capstoneProject.entity.AuthToken;

import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.entity.UserProfile;
import Capstone.capstoneProject.enums.UserRole;
import Capstone.capstoneProject.exceptions.RefreshTokenNotFoundException;
import Capstone.capstoneProject.repository.AuthTokenRepository;

import Capstone.capstoneProject.repository.UserProfileRepository;
import Capstone.capstoneProject.repository.UserRepository;
import Capstone.capstoneProject.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthTokenRepository authTokenRepository;

    @Transactional
    public TokenResponse login(LoginRequest request) {
        Users user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("계정이 존재하지 않습니다."));

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new IllegalArgumentException("비밀번호가 틀립니다.");
        } catch (Exception e) {
            throw new IllegalArgumentException("로그인 실패: " + e.getMessage());
        }
        String accessToken = jwtTokenProvider.createToken(authentication.getName());
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication.getName());


        // 트랜잭션 내에서 기존 token 삭제 + 새 token 저장
        authTokenRepository.deleteByUser(user);

        AuthToken tokenEntity = AuthToken.builder()
                .user(user)
                .refreshToken(refreshToken)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .createdAt(LocalDateTime.now())
                .build();
        authTokenRepository.save(tokenEntity);

        return new TokenResponse(accessToken, refreshToken);
    }

    @Transactional
    public TokenResponse autoLogin(AutoLoginRequest request) {
        String refreshToken = request.getRefreshToken();
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new RefreshTokenNotFoundException("유효하지 않은 리프레시 토큰입니다.");
        }
        // 토큰으로 사용자 추출
        String email = jwtTokenProvider.getUsername(refreshToken);

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("계정이 존재하지 않습니다."));
        // 새로운 accessToken, RefreshToken 생성
        String newAccessToken = jwtTokenProvider.createToken(user.getEmail());
        String newRefreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());
        return new TokenResponse(newAccessToken, newRefreshToken);
    }

}






package Capstone.capstoneProject.service;

import Capstone.capstoneProject.dto.*;
import Capstone.capstoneProject.entity.AuthToken;

import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.entity.UserProfile;
import Capstone.capstoneProject.enums.UserRole;
import Capstone.capstoneProject.exceptions.RefreshTokenNotFoundException;
import Capstone.capstoneProject.global.ApiResponse;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthTokenRepository authTokenRepository;
    private final UserProfileRepository userProfileRepository;

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

    @Transactional
    public TokenResponse googleLogin(OauthRequest request) {
        String provider = "google";
        String email = request.getEmail();
        String nickname = request.getNickname();

        Optional<Users> optionalUser = userRepository.findByEmail(email);
        Users user;
        UserProfile profile;
        // db에 사용자가 없을 때 회원가입된 후 토큰 반환
        if (optionalUser.isEmpty()) {
            user = Users.builder()
                    .email(email) // 로그인 아이디
                    .password("SOCIAL_LOGIN") // 소셜 로그인용 임의 비번
                    .provider(provider)
                    .providerId(email)
                    .role(UserRole.USER)
                    .build();
            userRepository.save(user);

            profile = UserProfile.builder() // 상태메시지랑, 프로필 이미지 null
                    .user(user)
                    .nickname(nickname)
                    .build();
            userProfileRepository.save(profile);

        } else {
            user = optionalUser.get();
            // 기존 유저라면 provider 정보 업데이트
            if (!user.getProvider().equals(provider)) {
                user.setProvider(provider);
                user.setProviderId(email);
            }
            // 기존 refresh token 삭제
            authTokenRepository.deleteByUser(user);
        }
        // JWT 발급
        String accessToken = jwtTokenProvider.createToken(user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

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
    public TokenResponse kakaoLogin(OauthRequest request) {
        String provider = "kakao";
        String email = request.getEmail();
        String nickname = request.getNickname();

        Optional<Users> optionalUser = userRepository.findByEmail(email);
        Users user;
        UserProfile profile;
        // db에 사용자가 없을 때 회원가입된 후 토큰 반환
        if (optionalUser.isEmpty()) {
            user = Users.builder()
                    .email(email) // 로그인 아이디
                    .password("SOCIAL_LOGIN") // 소셜 로그인용 임의 비번
                    .provider(provider)
                    .providerId(email)
                    .role(UserRole.USER)
                    .build();
            userRepository.save(user);

            profile = UserProfile.builder() // 상태메시지랑, 프로필 이미지 null
                    .user(user)
                    .nickname(nickname)
                    .build();
            userProfileRepository.save(profile);

        } else {
            user = optionalUser.get();
            // 기존 유저라면 provider 정보 업데이트
            if (!user.getProvider().equals(provider)) {
                user.setProvider(provider);
                user.setProviderId(email);
            }
            // 기존 refresh token 삭제
            authTokenRepository.deleteByUser(user);
        }
        // JWT 발급
        String accessToken = jwtTokenProvider.createToken(user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

        AuthToken tokenEntity = AuthToken.builder()
                .user(user)
                .refreshToken(refreshToken)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .createdAt(LocalDateTime.now())
                .build();
        authTokenRepository.save(tokenEntity);
        return new TokenResponse(accessToken, refreshToken);
    }

    }








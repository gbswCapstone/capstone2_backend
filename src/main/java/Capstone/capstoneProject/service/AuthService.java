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
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;


    private final AuthTokenRepository authTokenRepository;
    //회원가입
    public void signup(SecuritySignupRequest request) {
         if(userRepository.findByEmail(request.getEmail()).isPresent()) {
             throw new IllegalArgumentException("이미 존재하는 계정입니다.");
         }
        if (!request.getPassword().equals(request.getPasswordCheck())) {
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

         Users user = Users.builder()
                 .email(request.getEmail())
                 .password(passwordEncoder.encode(request.getPassword()))
                 .role(UserRole.USER)
                 .provider("local")
                 .providerId(request.getEmail()) // 고유 Id
                 .build();
        userRepository.save(user);

        UserProfile profile = UserProfile.builder()
                .nickname(request.getNickname())
                .statusMessage(request.getStatusMessage())
                .profileImg(request.getProfileImg())
                .user(user)
                .build();

        userProfileRepository.save(profile);
    }

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






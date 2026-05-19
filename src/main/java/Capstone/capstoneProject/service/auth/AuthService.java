package Capstone.capstoneProject.service.auth;

import Capstone.capstoneProject.dto.*;
import Capstone.capstoneProject.entity.auth.AuthToken;

import Capstone.capstoneProject.entity.chatbot.ChatBotRooms;
import Capstone.capstoneProject.entity.user.UserProfile;
import Capstone.capstoneProject.entity.user.Users;
import Capstone.capstoneProject.enums.UserRole;
import Capstone.capstoneProject.exceptions.unauthorized.PasswordMismatchException;
import Capstone.capstoneProject.exceptions.notfound.RefreshTokenNotFoundException;
import Capstone.capstoneProject.exceptions.notfound.UserNotFoundException;
import Capstone.capstoneProject.repository.auth.AuthTokenRepository;
import Capstone.capstoneProject.repository.user.UserProfileRepository;
import Capstone.capstoneProject.repository.user.UserRepository;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import Capstone.capstoneProject.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthTokenRepository authTokenRepository;
    private final UserProfileRepository userProfileRepository;
    private final AuthenticatedUserUtils authenticatedUserUtils;
    private final ChatBotService chatBotService;
    private final MissionService missionService;


    public LoginResponse login(LoginRequest request) {
        Users user = userRepository.findByEmailAndDeletedAtIsNull(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("계정이 존재하지 않습니다."));
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");
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

        // 챗봇방 생성 or 넘어가기
        ChatBotRooms chatBotRooms = chatBotService.createRoom(user);
        // 출석 미션 생성
        missionService.ensureAttendanceMission(user);

        return new LoginResponse(accessToken, refreshToken, chatBotRooms.getChatBotRoomId());
    }


    public LoginResponse autoLogin(AutoLoginRequest request) {
        String refreshToken = request.getRefreshToken();
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new RefreshTokenNotFoundException("유효하지 않은 리프레시 토큰입니다.");
        }

        AuthToken existingToken = authTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RefreshTokenNotFoundException("유효하지 않은 리프레시 토큰입니다."));
        // 토큰으로 사용자 추출
        String email = jwtTokenProvider.getUsername(refreshToken);

        Users user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new UserNotFoundException("계정이 존재하지 않습니다."));
        // 새로운 accessToken, RefreshToken 생성
        String newAccessToken = jwtTokenProvider.createToken(user.getEmail());
        String newRefreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

        authTokenRepository.deleteByUser(user);

        AuthToken tokenEntity = AuthToken.builder()
                .user(user)
                .refreshToken(newRefreshToken)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .createdAt(LocalDateTime.now())
                .build();
        authTokenRepository.save(tokenEntity);

        // 없으면 생성
        ChatBotRooms chatBotRooms = chatBotService.createRoom(user);

        return new LoginResponse(newAccessToken, newRefreshToken, chatBotRooms.getChatBotRoomId());
    }


    public LoginResponse googleLogin(OauthRequest request) {
        String provider = "google";
        String email = request.getEmail();
        String nickname = request.getNickname();

        Optional<Users> optionalUser = userRepository.findByEmailAndDeletedAtIsNull(email);
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
        ChatBotRooms chatBotRooms = chatBotService.createRoom(user);
        missionService.ensureAttendanceMission(user);

        return new LoginResponse(accessToken, refreshToken, chatBotRooms.getChatBotRoomId());
    }


    public LoginResponse kakaoLogin(OauthRequest request) {
        String provider = "kakao";
        String email = request.getEmail();
        String nickname = request.getNickname();

        Optional<Users> optionalUser = userRepository.findByEmailAndDeletedAtIsNull(email);
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
        ChatBotRooms chatBotRooms = chatBotService.createRoom(user);
        missionService.ensureAttendanceMission(user);
        return new LoginResponse(accessToken, refreshToken, chatBotRooms.getChatBotRoomId());
    }


    public void logOut() {
        Users user = authenticatedUserUtils.getCurrentUser();
        authTokenRepository.deleteByUser(user); // 토큰 삭제
    }

}








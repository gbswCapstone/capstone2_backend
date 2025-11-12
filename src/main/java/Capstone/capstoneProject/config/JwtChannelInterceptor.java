package Capstone.capstoneProject.config;

import Capstone.capstoneProject.entity.UserProfile;
import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.exceptions.UserNotFoundException;
import Capstone.capstoneProject.repository.UserProfileRepository;
import Capstone.capstoneProject.repository.UserRepository;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import Capstone.capstoneProject.security.CustomSecurityUserDetails;
import Capstone.capstoneProject.security.JwtTokenProvider;
import Capstone.capstoneProject.security.oauth.CustomOauth2UserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final CustomSecurityUserDetails customSecurityUserDetails;
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private final AuthenticatedUserUtils authenticatedUserUtils;
    private final UserProfileRepository userProfileRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // CONNECT 요청이 아니면 그대로 반환
        if (!StompCommand.CONNECT.equals(accessor.getCommand())) {
            return message;
        }

        String token = extractToken(accessor);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            return null; // JWT 없거나 검증 실패 시 연결 차단
        }

        // JWT에서 username(email) 추출
        String username = jwtTokenProvider.getUsername(token);
        List<GrantedAuthority> authorities = jwtTokenProvider.getRoles(token);

        // UserDetails 타입 선택
        UserDetails userDetails;
        if (jwtTokenProvider.isOauthUser(token)) {
            // OAuth2사용자
            Users user = userRepository.findByEmailAndDeletedAtIsNull(username)
                    .orElseThrow(() -> new UserNotFoundException("OAuth2 사용자 없음"));

            userDetails = new CustomOauth2UserDetails(user, Map.of());
        } else {
            // 일반 사용자
            userDetails = customSecurityUserDetails.loadUserByUsername(username);
        }
        // WebSocket 세션에 인증 정보 저장
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        accessor.getSessionAttributes().put("user", authToken);

        // user 정보 가져오기 (baarer token에서 추출)
        Users user = authenticatedUserUtils.getCurrentUser();
        // userProfile 정보 가져오기
        UserProfile profile = userProfileRepository.findByUserId(user.getId());

        accessor.getSessionAttributes().put("nickname", profile.getNickname());

        // 프론트에서 roomId 전달 시 세션에 저장
        accessor.getSessionAttributes().put("roomId", accessor.getFirstNativeHeader("roomId"));
        accessor.setUser(authToken);

        return message;
    }



    private String extractToken(StompHeaderAccessor accessor) {
        String header = accessor.getFirstNativeHeader(AUTH_HEADER);
        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            return null;
        }
        return header.substring(BEARER_PREFIX.length());
    }
}
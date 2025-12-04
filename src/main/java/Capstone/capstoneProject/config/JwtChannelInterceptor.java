package Capstone.capstoneProject.config;

import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.exceptions.InvalidJwtAuthenticationException;
import Capstone.capstoneProject.exceptions.UserNotFoundException;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final CustomSecurityUserDetails customSecurityUserDetails;
    private final AuthenticatedUserUtils authenticatedUserUtils;

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // CONNECT 요청 처리
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = extractToken(accessor);

            if (token == null || !jwtTokenProvider.validateToken(token)) {
                throw new InvalidJwtAuthenticationException("JWT 토큰이 없거나 유효하지 않습니다.");
            }

            String username = jwtTokenProvider.getUsername(token);
            UserDetails userDetails;

            if (jwtTokenProvider.isOauthUser(token)) {
                Users user = userRepository.findByEmailAndDeletedAtIsNull(username)
                        .orElseThrow(() -> new UserNotFoundException("OAuth2 사용자 없음"));
                userDetails = new CustomOauth2UserDetails(user, Map.of());
            } else {
                userDetails = customSecurityUserDetails.loadUserByUsername(username);
            }

            Users user = authenticatedUserUtils.getCurrentUser();
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            accessor.setUser(authenticationToken);

            // 세션에 사용자 정보 저장
            accessor.getSessionAttributes().put("user", user);
            accessor.getSessionAttributes().put("nickname", user.getProfile().getNickname());
            accessor.getSessionAttributes().put("roomId", accessor.getFirstNativeHeader("roomId"));
        }

        return message;
    }

    private String extractToken(StompHeaderAccessor accessor) {
        String authHeader = accessor.getFirstNativeHeader(AUTH_HEADER);
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}


package Capstone.capstoneProject.security;

import Capstone.capstoneProject.exceptions.NotAuthenticatedException;
import Capstone.capstoneProject.security.oauth.CustomOauth2UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class SecurityUtils {

    private SecurityUtils() {}

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new NotAuthenticatedException("로그인이 필요합니다.");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomSecurityUserDetails securityUser) {
            // 일반 시큐리티 로그인
            return securityUser.getUser().getId();
        } else if (principal instanceof CustomOauth2UserDetails oauthUser) {
            // OAuth 로그인
            return oauthUser.getUser().getId();
        } else {
            throw new NotAuthenticatedException("로그인이 필요합니다.");
        }
    }
}

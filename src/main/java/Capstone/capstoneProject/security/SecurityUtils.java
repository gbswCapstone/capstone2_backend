package Capstone.capstoneProject.security;

import Capstone.capstoneProject.exceptions.NotAuthenticatedException;
import Capstone.capstoneProject.exceptions.UserNotFoundException;
import Capstone.capstoneProject.repository.UserRepository;
import Capstone.capstoneProject.security.oauth.CustomOauth2UserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    private final UserRepository userRepository;

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new NotAuthenticatedException("로그인이 필요합니다.");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof org.springframework.security.core.userdetails.User springUser) {
            return userRepository.findByEmailAndDeletedAtIsNull(springUser.getUsername())
                    .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."))
                    .getId();
        } else if (principal instanceof CustomOauth2UserDetails oauthUser) {
            return oauthUser.getUser().getId();
        } else {
            throw new NotAuthenticatedException("로그인이 필요합니다.");
        }
    }
}


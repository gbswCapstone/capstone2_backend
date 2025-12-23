package Capstone.capstoneProject.security;

import Capstone.capstoneProject.entity.Users.Users;
import Capstone.capstoneProject.exceptions.unauthorized.NotAuthenticatedException;
import Capstone.capstoneProject.repository.UserRepository;
import Capstone.capstoneProject.security.oauth.CustomOauth2UserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    private final UserRepository userRepository;

    public Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new NotAuthenticatedException("로그인이 필요합니다.");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof org.springframework.security.core.userdetails.User userDetails) {
            return userRepository.findByEmailAndDeletedAtIsNull(userDetails.getUsername())
                    .orElseThrow(() -> new NotAuthenticatedException("사용자를 찾을 수 없습니다."));
        } else if (principal instanceof CustomOauth2UserDetails oauthUser) {
            return oauthUser.getUser();
        } else {
            throw new NotAuthenticatedException("로그인이 필요합니다.");
        }
    }
}


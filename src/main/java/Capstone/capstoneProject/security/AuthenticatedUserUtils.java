package Capstone.capstoneProject.security;

import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.exceptions.UserNotFoundException;
import Capstone.capstoneProject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserUtils {
    private final SecurityUtils securityUtils;
    private final UserRepository userRepository; // Users 조회용

    public Long getCurrentUserId() {
        return securityUtils.getCurrentUser().getId();
    }

    public Users getCurrentUser() {
        return securityUtils.getCurrentUser();
    }
}

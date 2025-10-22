package Capstone.capstoneProject.security;

import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.exceptions.UserNotFoundException;
import Capstone.capstoneProject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserUtils {
    private final SecurityUtils securityUtils;
    private final UserRepository userRepository; // Users 조회용

    public Long getCurrentUserId() {
        return securityUtils.getCurrentUserId();
    }

    public Users getCurrentUser() {
        Long userId = securityUtils.getCurrentUserId();


        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
    }
}

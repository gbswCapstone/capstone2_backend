package Capstone.capstoneProject.service;

import Capstone.capstoneProject.dto.ProfilePatchDTO;
import Capstone.capstoneProject.dto.SecuritySignupRequest;
import Capstone.capstoneProject.dto.UserResponseDTO;
import Capstone.capstoneProject.entity.UserProfile;
import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.enums.UserRole;
import Capstone.capstoneProject.exceptions.UserNotFoundException;
import Capstone.capstoneProject.repository.UserProfileRepository;
import Capstone.capstoneProject.repository.UserRepository;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UserService {
    private final AuthenticatedUserUtils authenticatedUserUtils;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;

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

    // 마이프로필 조회
    public UserResponseDTO getMyProfile() {
        // user 정보 가져오기 (baarer token에서 추출)
        Users user = authenticatedUserUtils.getCurrentUser();
        // userProfile 정보 가져오기
        UserProfile profile = userProfileRepository.findByUserId(user.getId());
        // 엔티티 → DTO 변환
        UserResponseDTO dto = new UserResponseDTO(user, profile);
        return dto;
    }

    // 마이프로필 수정
    public ProfilePatchDTO patchMyProfile(ProfilePatchDTO dto) {
        // user 정보 가져오기 (baarer token에서 추출)
        Users user = authenticatedUserUtils.getCurrentUser();
        // userProfile 정보 가져오기
        UserProfile profile = userProfileRepository.findByUserId(user.getId());
        user.setPassword(dto.getPassword());
        profile.setNickname(dto.getNickname());
        profile.setProfileImg(dto.getProfileImg());
        profile.setStatusMessage(dto.getStatusMessage());
        // db에 저장
        userRepository.save(user);
        userProfileRepository.save(profile);
        ProfilePatchDTO result = new ProfilePatchDTO(user, profile);
        return result;
    }
}

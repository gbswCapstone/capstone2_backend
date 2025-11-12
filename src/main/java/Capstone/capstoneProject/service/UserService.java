package Capstone.capstoneProject.service;

import Capstone.capstoneProject.dto.ProfilePatchDTO;
import Capstone.capstoneProject.dto.SecuritySignupRequest;
import Capstone.capstoneProject.dto.UserDeleteDTO;
import Capstone.capstoneProject.dto.UserResponseDTO;
import Capstone.capstoneProject.entity.UserProfile;
import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.enums.UserRole;
import Capstone.capstoneProject.repository.UserProfileRepository;
import Capstone.capstoneProject.repository.UserRepository;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor

public class UserService {
    private final AuthenticatedUserUtils authenticatedUserUtils;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;

    //회원가입
    public void signup(SecuritySignupRequest request) {
        if(userRepository.findByEmailAndDeletedAtIsNull(request.getEmail()).isPresent()) {
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
                .user(user).build();

        userProfileRepository.save(profile);
    }

    // 마이프로필 조회
    public UserResponseDTO getMyProfile() {
        // user 정보 가져오기 (baarer token에서 추출)
        Users user = authenticatedUserUtils.getCurrentUser();
        // userProfile 정보 가져오기
        UserProfile profile = userProfileRepository.findByUserId(user.getId());
        // 엔티티 → DTO 변환
        UserResponseDTO result = new UserResponseDTO(user, profile);
        return result;
    }

    // 마이프로필 수정
    public ProfilePatchDTO patchMyProfile(ProfilePatchDTO dto) {
        // user 정보 가져오기 (baarer token에서 추출)
        Users user = authenticatedUserUtils.getCurrentUser();
        // userProfile 정보 가져오기
        UserProfile profile = userProfileRepository.findByUserId(user.getId());
        // 비밀번호 수정 시 암호화 적용
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(dto.getPassword());
            user.setPassword(encodedPassword);
        }
        profile.setNickname(dto.getNickname());
        profile.setProfileImg(dto.getProfileImg());
        profile.setStatusMessage(dto.getStatusMessage());
        // db에 저장
        userRepository.save(user);
        userProfileRepository.save(profile);
        ProfilePatchDTO result = new ProfilePatchDTO(user, profile);
        return result;
    }

    @Transactional
    public UserDeleteDTO deleteUser() {
        // user 정보 가져오기 (baarer token에서 추출)
        Users user = authenticatedUserUtils.getCurrentUser();
        UserProfile profile = userProfileRepository.findByUserId(user.getId());
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);

        return UserDeleteDTO.builder()
                .email(user.getEmail())
                .nickname(profile.getNickname())
                .profileImg(profile.getProfileImg())
                .deletedAt(user.getDeletedAt())
                .build();
    }
}

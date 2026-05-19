package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.config.QuerydslConfiguration;
import Capstone.capstoneProject.entity.Users.UserProfile;
import Capstone.capstoneProject.entity.Users.Users;
import Capstone.capstoneProject.enums.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import(QuerydslConfiguration.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Test
    @DisplayName("유저 저장 및 이메일로 조회")
    void saveAndFindByEmail() {
        Users user = Users.builder()
                .email("test@example.com")
                .password("encoded_password")
                .provider("local")
                .providerId("test@example.com")
                .role(UserRole.USER)
                .build();

        userRepository.save(user);

        Optional<Users> found = userRepository.findByEmailAndDeletedAtIsNull("test@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
        assertThat(found.get().getRole()).isEqualTo(UserRole.USER);
    }

    @Test
    @DisplayName("삭제된 유저는 조회되지 않음 (소프트 삭제 검증)")
    void deletedUserNotFound() {
        Users user = Users.builder()
                .email("deleted@example.com")
                .password("pw")
                .provider("local")
                .providerId("deleted@example.com")
                .role(UserRole.USER)
                .build();
        userRepository.save(user);

        user.setDeletedAt(java.time.LocalDateTime.now());
        userRepository.save(user);

        Optional<Users> found = userRepository.findByEmailAndDeletedAtIsNull("deleted@example.com");
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 이메일 조회 시 빈 Optional 반환")
    void findByNonExistentEmail() {
        Optional<Users> found = userRepository.findByEmailAndDeletedAtIsNull("notexist@example.com");
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("유저 프로필 저장 및 조회")
    void saveAndFindUserProfile() {
        Users user = Users.builder()
                .email("profile@example.com")
                .password("pw")
                .provider("local")
                .providerId("profile@example.com")
                .role(UserRole.USER)
                .build();
        userRepository.save(user);

        UserProfile profile = UserProfile.builder()
                .user(user)
                .nickname("테스트닉네임")
                .statusMessage("안녕하세요")
                .build();
        userProfileRepository.save(profile);

        UserProfile found = userProfileRepository.findByUserId(user.getId());
        assertThat(found).isNotNull();
        assertThat(found.getNickname()).isEqualTo("테스트닉네임");
    }
}

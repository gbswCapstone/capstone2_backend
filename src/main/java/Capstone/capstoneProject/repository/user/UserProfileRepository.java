package Capstone.capstoneProject.repository.user;


import Capstone.capstoneProject.entity.user.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    UserProfile findByUserId(Long userId);

    // user가 삭제되지 않은 경우만 조회
    UserProfile findByUserIdAndUserDeletedAtIsNull(Long userId);
}

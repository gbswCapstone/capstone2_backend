package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
//    Optional<Users> findById(Long id);
//    boolean existsByEmail (String email);
    // 삭제되지 않은 유저를 이메일로 조회
    Optional<Users> findByEmailAndDeletedAtIsNull(String email);
//
    Optional<Users> findByIdAndDeletedAtIsNull(Long id);

}

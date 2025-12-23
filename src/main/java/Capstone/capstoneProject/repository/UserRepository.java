package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.Users.Users;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
;
    // 삭제되지 않은 유저를 이메일로 조회
    @EntityGraph(attributePaths = {"profile"})
    Optional<Users> findByEmailAndDeletedAtIsNull(String email);

    @EntityGraph(attributePaths = {"profile"})
    Optional<Users> findByIdAndDeletedAtIsNull(Long id);

}

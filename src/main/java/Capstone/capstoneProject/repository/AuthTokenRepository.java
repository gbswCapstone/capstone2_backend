package Capstone.capstoneProject.repository;


import Capstone.capstoneProject.entity.AuthToken;
import Capstone.capstoneProject.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface AuthTokenRepository extends JpaRepository<AuthToken, Long> {

    // 유저의 기존 토큰 삭제
    void deleteByUser(Users user);

    // refresh token으로 토큰 조회 (로그아웃, 재발급용)
    Optional<AuthToken> findByRefreshToken(String refreshToken);
}

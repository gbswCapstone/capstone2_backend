package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.Users.Users;
import Capstone.capstoneProject.entity.challenges.Challenges;
import Capstone.capstoneProject.entity.challenges.ChallengeLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<ChallengeLikes, Long> {
    Optional<ChallengeLikes> findByUserAndChallenges(Users user, Challenges challenges);
    int countByChallenges(Challenges challenges);
    void deleteByUserAndChallenges(Users user, Challenges challenges);

    List<ChallengeLikes> findAllByUserOrderByCreatedAtDesc(Users user);
}

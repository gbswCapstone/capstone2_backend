package Capstone.capstoneProject.repository.challenge;

import Capstone.capstoneProject.entity.user.Users;
import Capstone.capstoneProject.entity.challenge.Challenges;
import Capstone.capstoneProject.entity.challenge.ChallengeLikes;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<ChallengeLikes, Long> {
    Optional<ChallengeLikes> findByUserAndChallenges(Users user, Challenges challenges);
    int countByChallenges(Challenges challenges);
    void deleteByUserAndChallenges(Users user, Challenges challenges);

    @EntityGraph(attributePaths = {"challenges", "challenges.challengeUsers", "challenges.challengeHashtags", "challenges.challengeHashtags.hashtag"})
    List<ChallengeLikes> findAllByUserOrderByCreatedAtDesc(Users user);
}

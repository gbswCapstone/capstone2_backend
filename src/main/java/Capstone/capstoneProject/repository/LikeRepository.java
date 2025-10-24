package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.entity.challenges.Challenges;
import Capstone.capstoneProject.entity.challenges.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByUserAndChallenges(Users user, Challenges challenges);
    int countByChallenges(Challenges challenges);
    void deleteByUserAndChallenges(Users user, Challenges challenges);
}

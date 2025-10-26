package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.challenges.ChallengeUsers;
import Capstone.capstoneProject.entity.challenges.Challenges;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeUsersRepository extends JpaRepository<ChallengeUsers, Long> {
    boolean existsByChallengeIdAndUserId(Long challengeId, Long userId);
    int countByChallengeId(Long challengeId);
    List<ChallengeUsers> findByUserId(Long userId);
}

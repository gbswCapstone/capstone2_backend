package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.challenges.ChallengeUsers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeUsersRepository extends JpaRepository<ChallengeUsers, Long> {
    boolean existsByChallengeIdAndUserId(Long challengeId, Long userId);
    int countByChallengeId(Long challengeId);
}

package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.challenges.ChallengeUsers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChallengeUsersRepository extends JpaRepository<ChallengeUsers, Long> {
    boolean existsByChallengeIdAndUserId(Long challengeId, Long userId);

    int countByChallenge_IdAndChallenge_DeletedAtIsNull(Long challengeId);
    Optional<ChallengeUsers> findByChallengeIdAndUserId(Long challengeId, Long userId);
    List<ChallengeUsers> findByUserId(Long userId);
}

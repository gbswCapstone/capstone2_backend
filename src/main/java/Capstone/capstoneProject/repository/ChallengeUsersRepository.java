package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.challenges.ChallengeUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ChallengeUsersRepository extends JpaRepository<ChallengeUsers, Long> {
    boolean existsByChallengeIdAndUserId(Long challengeId, Long userId);

    int countByChallenge_IdAndChallenge_DeletedAtIsNull(Long challengeId);
    Optional<ChallengeUsers> findByChallengeIdAndUserId(Long challengeId, Long userId);
    List<ChallengeUsers> findByUserId(Long userId);

    // N+1 방지: 여러 챌린지에 대한 참여 여부를 한 번에 조회
    @Query("SELECT cu.challenge.id FROM ChallengeUsers cu WHERE cu.user.id = :userId AND cu.challenge.id IN :challengeIds")
    Set<Long> findJoinedChallengeIdsByUserIdAndChallengeIds(@Param("userId") Long userId, @Param("challengeIds") List<Long> challengeIds);
}

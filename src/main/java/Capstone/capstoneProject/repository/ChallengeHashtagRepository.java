package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.challenges.ChallengeHashtag;
import Capstone.capstoneProject.entity.challenges.Challenges;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeHashtagRepository extends JpaRepository<ChallengeHashtag, Long> {
    List<ChallengeHashtag> findByChallenge(Challenges challenge);
}

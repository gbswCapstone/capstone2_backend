package Capstone.capstoneProject.repository.challenge;

import Capstone.capstoneProject.entity.challenge.ChallengeHashtag;
import Capstone.capstoneProject.entity.challenge.Challenges;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeHashtagRepository extends JpaRepository<ChallengeHashtag, Long> {
    List<ChallengeHashtag> findByChallenge(Challenges challenge);
}

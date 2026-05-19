package Capstone.capstoneProject.repository;


import Capstone.capstoneProject.entity.challenges.Challenges;
import Capstone.capstoneProject.enums.UserJobs;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenges, Long>, ChallengeRepositoryCustom {

    // job 없이 조회 최신순
    @EntityGraph(attributePaths = {"challengeUsers", "challengeHashtags", "challengeHashtags.hashtag"})
    List<Challenges> findAllByDeletedAtIsNullOrderByCreatedAtDesc();

    // job 없이 조회 오래된순
    @EntityGraph(attributePaths = {"challengeUsers", "challengeHashtags", "challengeHashtags.hashtag"})
    List<Challenges> findAllByDeletedAtIsNullOrderByCreatedAtAsc();

    // job 없이 인기순(좋아요순) + 최신순
    @EntityGraph(attributePaths = {"challengeUsers", "challengeHashtags", "challengeHashtags.hashtag"})
    List<Challenges> findAllByDeletedAtIsNullOrderByLikeCountDescCreatedAtDesc();

    // job 조회 인기순(좋아요순) + 최신순
    @EntityGraph(attributePaths = {"challengeUsers", "challengeHashtags", "challengeHashtags.hashtag"})
    List<Challenges> findAllByJobAndDeletedAtIsNullOrderByLikeCountDescCreatedAtDesc(UserJobs job);

    // job 조회 최신순
    @EntityGraph(attributePaths = {"challengeUsers", "challengeHashtags", "challengeHashtags.hashtag"})
    List<Challenges> findAllByJobAndDeletedAtIsNullOrderByCreatedAtDesc(UserJobs job);

    // job 조회 오래된순
    @EntityGraph(attributePaths = {"challengeUsers", "challengeHashtags", "challengeHashtags.hashtag"})
    List<Challenges> findAllByJobAndDeletedAtIsNullOrderByCreatedAtAsc(UserJobs job);

    Optional<Challenges> findByIdAndDeletedAtIsNull(Long challengeId);

}



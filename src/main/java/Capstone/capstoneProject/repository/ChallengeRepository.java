package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.dto.Challenges.ChallengeListDTO;
import Capstone.capstoneProject.entity.challenges.Challenges;
import Capstone.capstoneProject.enums.UserJobs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenges, Long>, ChallengeRepositoryCustom {

    // job 없이 조회 최신순
    List<Challenges> findAllByOrderByCreatedAtDesc();

    // job 없이 조회 오래된순
    List<Challenges> findAllByOrderByCreatedAtAsc();

    // job 없이 인기순(좋아요순) + 최신순-
    List<Challenges> findAllByOrderByLikeCountDescCreatedAtDesc();

    // job 조회 인기순(좋아요순) + 최신순
    List<Challenges> findAllByJobOrderByLikeCountDescCreatedAtDesc(UserJobs job);

    // job 조회 최신순

    List<Challenges> findAllByJobOrderByCreatedAtDesc(UserJobs job);

    // job 조회 오래된순
    List<Challenges> findAllByJobOrderByCreatedAtAsc(UserJobs job);


    Optional<Challenges> findByIdAndDeletedAtIsNull(Long challengeId);

}



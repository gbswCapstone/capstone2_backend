package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.dto.Challenges.ChallengeListDTO;
import Capstone.capstoneProject.entity.challenges.Challenges;
import Capstone.capstoneProject.enums.UserJobs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenges, Long> {

    // job 없이 조회 최신순
    @Query("""
    SELECT DISTINCT c
    FROM Challenges c
    LEFT JOIN FETCH c.challengeUsers cu
    WHERE c.deletedAt IS NULL
    ORDER BY c.createdAt DESC
""")
    List<Challenges> findAllActiveOrderByCreatedAtDesc();

    // job 없이 조회 최신순
    @Query("""
    SELECT DISTINCT c
    FROM Challenges c
    LEFT JOIN FETCH c.challengeUsers cu
    WHERE c.deletedAt IS NULL
    ORDER BY c.createdAt DESC
""")
    List<Challenges> findAllActiveOrderByCreatedAtAsc();

    // job 없이 인기순(좋아요순)
    @Query("""
    SELECT DISTINCT c
    FROM Challenges c
    LEFT JOIN FETCH c.challengeUsers cu
    WHERE c.deletedAt IS NULL
    ORDER BY c.likeCount DESC
""")
    List<Challenges> findAllActiveOrderByLikeCountDesc();

    // job 없이 인기순(좋아요순)
    @Query("""
    SELECT DISTINCT c
    FROM Challenges c
    LEFT JOIN FETCH c.challengeUsers cu
    WHERE c.deletedAt IS NULL
    ORDER BY c.likeCount DESC
""")
    List<Challenges> findAllActiveByJobOrderByCreatedAtDesc(UserJobs job);

    // job 검색 오래된순
    @Query("""
    SELECT DISTINCT c
    FROM Challenges c
    LEFT JOIN FETCH c.challengeUsers cu
    WHERE c.deletedAt IS NULL AND c.job = :job
    ORDER BY c.createdAt ASC
""")
    List<Challenges> findAllActiveByJobOrderByCreatedAtAsc(UserJobs job);

    // job 검색 오래된순
    @Query("""
    SELECT DISTINCT c
    FROM Challenges c
    LEFT JOIN FETCH c.challengeUsers cu
    WHERE c.deletedAt IS NULL AND c.job = :job
    ORDER BY c.createdAt ASC
""")
    List<Challenges> findAllActiveByJobOrderByLikeCountDesc(UserJobs job);

    // 해시태그
    @Query("""
    SELECT DISTINCT c
    FROM Challenges c
    JOIN c.challengeHashtags ch
    JOIN ch.hashtag h
    WHERE h.name LIKE CONCAT('%', :keyword, '%')
""")
    List<Challenges> findByHashtagNameContaining(@Param("keyword") String keyword);

    //제목
    @Query("""
    SELECT DISTINCT c
    FROM Challenges c
    WHERE c.title LIKE CONCAT('%', :keyword, '%')
""")
    List<Challenges> findByTitleContaining(@Param("keyword") String keyword);

    // 해시태그+키워드
    @Query("""
    SELECT DISTINCT c
    FROM Challenges c
    JOIN c.challengeHashtags ch
    JOIN ch.hashtag h
    WHERE h.name LIKE CONCAT('%', :hashtag, '%')
      AND c.title LIKE CONCAT('%', :keyword, '%')
""")
    List<Challenges> searchByHashtagAndKeyword(@Param("hashtag") String hashtag,
                                               @Param("keyword") String keyword);

}



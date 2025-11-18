package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.dto.Challenges.ChallengeListDTO;
import Capstone.capstoneProject.entity.challenges.Challenges;
import Capstone.capstoneProject.enums.UserJobs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenges, Long> {

    // job없이 조회 최신순
    @Query("""
    SELECT new Capstone.capstoneProject.dto.Challenges.ChallengeListDTO(
        c.id,
        c.title,
        c.maxPersonnel,
        COUNT(cu.id),
        c.likeCount
    )
    FROM Challenges c
    LEFT JOIN ChallengeUsers cu ON cu.challenge.id = c.id
    WHERE c.deletedAt IS NULL
    GROUP BY c.id, c.title, c.maxPersonnel, c.likeCount
    ORDER BY c.createdAt DESC
""")
    List<ChallengeListDTO> findAllActiveOrderByCreatedAtDescWithCurrentPersonnel();


    // job없이 조회 인기순(좋아요순)
    @Query("""
    SELECT new Capstone.capstoneProject.dto.Challenges.ChallengeListDTO(
        c.id,
        c.title,
        c.maxPersonnel,
        COUNT(cu.id),
        c.likeCount
    )
    FROM Challenges c
    LEFT JOIN ChallengeUsers cu ON cu.challenge.id = c.id
    WHERE c.deletedAt IS NULL
    GROUP BY c.id, c.title, c.maxPersonnel, c.likeCount
    ORDER BY c.likeCount DESC
""")
    List<ChallengeListDTO> findAllActiveOrderByLikeCountDescWithCurrentPersonnel();



    // job 검색 최신순
    @Query("""
    SELECT new Capstone.capstoneProject.dto.Challenges.ChallengeListDTO(
        c.id,
        c.title,
        c.maxPersonnel,
        COUNT(cu.id),
        c.likeCount
    )
    FROM Challenges c
    LEFT JOIN ChallengeUsers cu ON cu.challenge.id = c.id
    WHERE c.deletedAt IS NULL AND c.job = :job
    GROUP BY c.id, c.title, c.maxPersonnel, c.likeCount
    ORDER BY c.createdAt DESC
""")
    List<ChallengeListDTO> findAllActiveByJobOrderByCreatedAtDescWithCurrentPersonnel(UserJobs job);



    // job 검색 인기순(좋아요순)
    @Query("""
    SELECT new Capstone.capstoneProject.dto.Challenges.ChallengeListDTO(
        c.id,
        c.title,
        c.maxPersonnel,
        COUNT(cu.id),
        c.likeCount
    )
    FROM Challenges c
    LEFT JOIN ChallengeUsers cu ON cu.challenge.id = c.id
    WHERE c.deletedAt IS NULL AND c.job = :job
    GROUP BY c.id, c.title, c.maxPersonnel, c.likeCount
    ORDER BY c.likeCount DESC
""")
    List<ChallengeListDTO> findAllActiveByJobOrderByLikeCountDescWithCurrentPersonnel(UserJobs job);

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



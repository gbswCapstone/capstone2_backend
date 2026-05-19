package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.Boards.BoardLikes;
import Capstone.capstoneProject.entity.Boards.Boards;
import Capstone.capstoneProject.entity.Users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BoardLikeRepository extends JpaRepository<BoardLikes, Long> {

    // 내 좋아요한 게시글 중 삭제되지 않은 게시글 조회 + 최신순(좋아요한 기준)
    List<BoardLikes> findByUsersAndBoards_DeletedAtIsNullOrderByCreatedAtDesc(Users user);

    // 내 좋아요한 게시글 중 삭제되지 않은 게시글 조회 + 오래된순(좋아요한 기준)
    List<BoardLikes> findByUsersAndBoards_DeletedAtIsNullOrderByCreatedAtAsc(Users user);

    // 내 좋아요한 게시글 중 삭제되지 않은 게시글 조회 + 좋아요순 + 최신순(좋아요한 기준)
    List<BoardLikes> findByUsersAndBoards_DeletedAtIsNullOrderByBoards_LikeCountDescCreatedAtDesc(Users user);

    Optional<BoardLikes> findByUsersAndBoardsAndBoards_DeletedAtIsNull(Users user, Boards boards);
    // 내가 좋아요한 게시물인지 판단
    boolean existsByUsersAndBoardsAndBoards_DeletedAtIsNull(Users user, Boards boards);

    // N+1 방지: 여러 게시글에 대한 좋아요 여부를 한 번에 조회
    @Query("SELECT bl.boards.id FROM BoardLikes bl WHERE bl.users = :user AND bl.boards.id IN :boardIds AND bl.boards.deletedAt IS NULL")
    Set<Long> findLikedBoardIdsByUserAndBoardIds(@Param("user") Users user, @Param("boardIds") List<Long> boardIds);
}

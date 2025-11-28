package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.Boards.BoardLikes;
import Capstone.capstoneProject.entity.Boards.Boards;
import Capstone.capstoneProject.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

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
}

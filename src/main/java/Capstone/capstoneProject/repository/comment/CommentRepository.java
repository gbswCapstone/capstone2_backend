package Capstone.capstoneProject.repository.comment;

import Capstone.capstoneProject.entity.comment.Comments;
import Capstone.capstoneProject.entity.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comments, Long> {

    // 유저의 댓글 조회 + 최신순
    List<Comments> findByUsersOrderByCreatedAtDesc(Users users);

    // 유저의 댓글 조회 + 좋아요순 + 최신순
    List<Comments> findByUsersOrderByLikeCountDescCreatedAtDesc(Users users);

    // 유저의 댓글 조회 + 오래된순
    List<Comments> findByUsersOrderByCreatedAtAsc(Users users);

    // 해당 게시글의 댓글 + 좋아요순 + 최신순
    List<Comments> findAllByBoardsIdOrderByLikeCountDescCreatedAtDesc(Long boardId);

    // 해당 게시글의 댓글 + 최신순
    List<Comments> findAllByBoardsIdOrderByCreatedAtDesc(Long boardId);

    // 해당 게시글의 댓글 + 오래된순
    List<Comments> findAllByBoardsIdOrderByCreatedAtAsc(Long boardId);

}

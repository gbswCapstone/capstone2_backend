package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.CommentLikes;
import Capstone.capstoneProject.entity.Comments;
import Capstone.capstoneProject.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLikes, Long> {

    Optional<CommentLikes> findByUsersAndComments(Users user, Comments comments);

    // 내가 좋아요한 댓글인지 판단
    boolean existsByUsersAndCommentsAndComments_Boards_DeletedAtIsNull(Users user, Comments comments);
}

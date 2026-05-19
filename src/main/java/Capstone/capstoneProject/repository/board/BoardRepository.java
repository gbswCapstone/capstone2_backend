package Capstone.capstoneProject.repository.board;

import Capstone.capstoneProject.entity.board.Boards;
import Capstone.capstoneProject.entity.user.Users;
import Capstone.capstoneProject.enums.BoardCategory;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Boards, Long> {
    Optional<Boards> findByIdAndDeletedAtIsNull(Long id);

    // 유저의 게시글 조회 + 최신순
    @EntityGraph(attributePaths = {"users", "users.profile", "boardImages"})
    List<Boards> findByUsersAndDeletedAtIsNullOrderByCreatedAtDesc(Users users);

    // 유저의 게시글 조회 + 오래된순
    @EntityGraph(attributePaths = {"users", "users.profile", "boardImages"})
    List<Boards> findByUsersAndDeletedAtIsNullOrderByCreatedAtAsc(Users users);

    // 최신순
    @EntityGraph(attributePaths = {"users", "users.profile", "boardImages"})
    List<Boards> findAllByDeletedAtIsNullOrderByCreatedAtDesc();

    // 오래된순
    @EntityGraph(attributePaths = {"users", "users.profile", "boardImages"})
    List<Boards> findAllByDeletedAtIsNullOrderByCreatedAtAsc();

    // 좋아요순 + 최신순
    @EntityGraph(attributePaths = {"users", "users.profile", "boardImages"})
    List<Boards> findAllByDeletedAtIsNullOrderByLikeCountDescCreatedAtDesc();

    // 카테고리 + 좋아요 + 최신순
    @EntityGraph(attributePaths = {"users", "users.profile", "boardImages"})
    List<Boards> findAllByCategoryAndDeletedAtIsNullOrderByLikeCountDescCreatedAtDesc(BoardCategory category);

    // 카테고리 + 오래된순
    @EntityGraph(attributePaths = {"users", "users.profile", "boardImages"})
    List<Boards> findAllByCategoryAndDeletedAtIsNullOrderByCreatedAtAsc(BoardCategory category);

    // 카테고리 + 최신순
    @EntityGraph(attributePaths = {"users", "users.profile", "boardImages"})
    List<Boards> findAllByCategoryAndDeletedAtIsNullOrderByCreatedAtDesc(BoardCategory category);

    // 제목으로 검색 + 최신순
    @EntityGraph(attributePaths = {"users", "users.profile", "boardImages"})
    List<Boards> findAllByDeletedAtIsNullAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(String keyword);

    // 제목으로 검색 + 좋아요순 + 최신순
    @EntityGraph(attributePaths = {"users", "users.profile", "boardImages"})
    List<Boards> findAllByDeletedAtIsNullAndTitleContainingIgnoreCaseOrderByLikeCountDescCreatedAtDesc(String keyword);

    // 제목으로 검색 + 오래된순
    @EntityGraph(attributePaths = {"users", "users.profile", "boardImages"})
    List<Boards> findAllByDeletedAtIsNullAndTitleContainingIgnoreCaseOrderByCreatedAtAsc(String keyword);

    // 제목으로 검색 + 카테고리 + 최신순
    @EntityGraph(attributePaths = {"users", "users.profile", "boardImages"})
    List<Boards> findAllByDeletedAtIsNullAndCategoryAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(BoardCategory category, String keyword);

    // 제목으로 검색 + 카테고리 + 좋아요순 + 최신순
    @EntityGraph(attributePaths = {"users", "users.profile", "boardImages"})
    List<Boards> findAllByDeletedAtIsNullAndCategoryAndTitleContainingIgnoreCaseOrderByLikeCountDescCreatedAtDesc(BoardCategory category, String keyword);

    // 제목으로 검색 + 카테고리 + 오래된순
    @EntityGraph(attributePaths = {"users", "users.profile", "boardImages"})
    List<Boards> findAllByDeletedAtIsNullAndCategoryAndTitleContainingIgnoreCaseOrderByCreatedAtAsc(BoardCategory category, String keyword);
}

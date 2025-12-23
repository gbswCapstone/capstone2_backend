package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.Boards.Boards;
import Capstone.capstoneProject.entity.Users.Users;
import Capstone.capstoneProject.enums.BoardCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Boards, Long> {
    Optional<Boards> findByIdAndDeletedAtIsNull(Long id);

    // 유저의 게시글 조회 + 최신순
    List<Boards> findByUsersAndDeletedAtIsNullOrderByCreatedAtDesc(Users users);

    // 유저의 게시글 조회 + 오래된순
    List<Boards> findByUsersAndDeletedAtIsNullOrderByCreatedAtAsc(Users users);

    // 최신순
    List<Boards> findAllByDeletedAtIsNullOrderByCreatedAtDesc();

    // 오래된순
    List<Boards> findAllByDeletedAtIsNullOrderByCreatedAtAsc();

    // 좋아요순 + 최신순
    List<Boards> findAllByDeletedAtIsNullOrderByLikeCountDescCreatedAtDesc();

    // 카테고리 + 좋아요 + 최신순
    List<Boards> findAllByCategoryAndDeletedAtIsNullOrderByLikeCountDescCreatedAtDesc(BoardCategory category);

    // 카테고리 + 오래된순
    List<Boards> findAllByCategoryAndDeletedAtIsNullOrderByCreatedAtAsc(BoardCategory category);

    // 카테고리 + 최신순
    List<Boards> findAllByCategoryAndDeletedAtIsNullOrderByCreatedAtDesc(BoardCategory category);


    // 제목으로 검색 + 최신순
    List<Boards> findAllByDeletedAtIsNullAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(String keyword);

    // 제목으로 검색 + 좋아요순 + 최신순
    List<Boards> findAllByDeletedAtIsNullAndTitleContainingIgnoreCaseOrderByLikeCountDescCreatedAtDesc(String keyword);

    // 제목으로 검색 + 오래된순
    List<Boards> findAllByDeletedAtIsNullAndTitleContainingIgnoreCaseOrderByCreatedAtAsc(String keyword);

    // 제목으로 검색 + 카테고리 + 최신순
    List<Boards> findAllByDeletedAtIsNullAndCategoryAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(BoardCategory category, String keyword);

    // 제목으로 검색 + 카테고리 + 좋아요순 + 최신순
    List<Boards> findAllByDeletedAtIsNullAndCategoryAndTitleContainingIgnoreCaseOrderByLikeCountDescCreatedAtDesc(BoardCategory category, String keyword);

    // 제목으로 검색 + 카테고리 + 오래된순
    List<Boards> findAllByDeletedAtIsNullAndCategoryAndTitleContainingIgnoreCaseOrderByCreatedAtAsc(BoardCategory category, String keyword);



}

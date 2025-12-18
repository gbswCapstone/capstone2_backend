package Capstone.capstoneProject.service;

import Capstone.capstoneProject.dto.Comments.CommentListDTO;
import Capstone.capstoneProject.dto.Comments.CommentResponse;
import Capstone.capstoneProject.dto.Comments.CreateComment;
import Capstone.capstoneProject.dto.Comments.PatchComment;
import Capstone.capstoneProject.dto.LikeResponseDTO;
import Capstone.capstoneProject.dto.UserSummaryDTO;
import Capstone.capstoneProject.entity.Boards.Boards;
import Capstone.capstoneProject.entity.CommentLikes;
import Capstone.capstoneProject.entity.Comments;
import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.enums.SortType;
import Capstone.capstoneProject.exceptions.notfound.BoardNotFoundException;
import Capstone.capstoneProject.exceptions.notfound.CommentNotFoundException;
import Capstone.capstoneProject.exceptions.forbidden.NotCommentOwnerException;
import Capstone.capstoneProject.repository.BoardRepository;
import Capstone.capstoneProject.repository.CommentLikeRepository;
import Capstone.capstoneProject.repository.CommentRepository;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final AuthenticatedUserUtils authenticatedUserUtils;
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final CommentLikeRepository commentLikeRepository;

    public CommentResponse createComment(CreateComment request, Long boardId) {
        Users user = authenticatedUserUtils.getCurrentUser();
        Boards board = boardRepository.findByIdAndDeletedAtIsNull(boardId)
                .orElseThrow(() -> new BoardNotFoundException("해당 게시물을 찾을 수 없습니다."));
        Comments parent = null;
        if (request.getParentId() != null) {
            parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글을 찾을 수 없습니다."));

            if (parent.getParentId() != null) {
                throw new IllegalArgumentException("대댓글에는 댓글을 달 수 없습니다.");
            }
        }
        board.setCommentCount(board.getCommentCount() + 1);
        boardRepository.save(board);
        Comments comments = Comments.builder()
                .content(request.getContent())
                .boards(board)
                .users(user)
                .parentId(parent)
                .likeCount(0)
                .build();

        Comments savedComment = commentRepository.save(comments);
        return CommentResponse.builder()
                .id(savedComment.getId())
                .users(UserSummaryDTO.from(user))
                .parentId(savedComment.getParentId() != null ? savedComment.getParentId().getId() : null)
                .content(savedComment.getContent())
                .likeCount(savedComment.getLikeCount())
                .isLiked(false) // 생성할 때니까
                .updatedAt(savedComment.getUpdatedAt())
                .build();
    }

    public List<CommentListDTO> getComments(Long boardId, SortType sortType) {
        Users user = authenticatedUserUtils.getCurrentUser();
        Boards boards = boardRepository.findByIdAndDeletedAtIsNull(boardId)
                .orElseThrow(() -> new BoardNotFoundException("해당 게시글을 찾을 수 없습니다."));
        // 기본 값 설정
        SortType finalSort = (sortType == null) ? SortType.RECENT : sortType;

        List<Comments> comments;

        if (finalSort == SortType.POPULAR) {
            comments = commentRepository.findAllByBoardsIdOrderByLikeCountDescCreatedAtDesc(boardId);
        } else if (finalSort == SortType.OLDEST) {
            comments = commentRepository.findAllByBoardsIdOrderByCreatedAtAsc(boardId);
        } else { // RECENT
            comments = commentRepository.findAllByBoardsIdOrderByCreatedAtDesc(boardId);
        }

        return CommentListDTO.fromComments(comments, user);
    }

    public CommentResponse patchComment(PatchComment request, Long id) {
        Users user = authenticatedUserUtils.getCurrentUser();

        Comments comments = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("해당 댓글을 찾을 수 없습니다."));

        if (!comments.getUsers().getId().equals(user.getId())) {
            throw new NotCommentOwnerException("댓글을 수정할 권한이 없습니다.");
        }

        comments.setContent(request.getContent());
        commentRepository.save(comments);

        boolean isLiked = commentLikeRepository.existsByUsersAndCommentsAndComments_Boards_DeletedAtIsNull(user, comments);

        return CommentResponse.builder()
                .id(comments.getId())
                .users(UserSummaryDTO.from(user))
                .parentId(comments.getParentId() != null ? comments.getParentId().getId() : null)
                .content(comments.getContent())
                .likeCount(comments.getLikeCount())
                .isLiked(isLiked)
                .updatedAt(comments.getUpdatedAt())
                .build();
    }


    public void deleteComment(Long id) {
        Users user = authenticatedUserUtils.getCurrentUser();
        Comments comments = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("해당 댓글을 찾을 수 없습니다."));
        if (!comments.getUsers().getId().equals(user.getId())) {
            throw new NotCommentOwnerException("댓글을 삭제할 권한이 없습니다.");
        }
        Boards boards = boardRepository.findById(comments.getBoards().getId())
                        .orElseThrow(() -> new BoardNotFoundException("해당 게시물을 찾을 수 없습니다."));

        boards.setCommentCount(boards.getCommentCount() - 1);
        boardRepository.save(boards);

        commentRepository.deleteById(id);
    }

    public LikeResponseDTO likeComment(Long id) {
        Users user = authenticatedUserUtils.getCurrentUser();

        Comments comments = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("해당 댓글을 찾을 수 없습니다."));

        boolean isLiked = commentLikeRepository.existsByUsersAndCommentsAndComments_Boards_DeletedAtIsNull(user, comments);

        if (isLiked) {
            CommentLikes commentLikes = commentLikeRepository.findByUsersAndComments(user, comments)
                    .orElseThrow(() -> new CommentNotFoundException("데이터 불일치 오류"));
            commentLikeRepository.delete(commentLikes);
            comments.setLikeCount(comments.getLikeCount() - 1);

        } else {
            CommentLikes commentLike = CommentLikes.builder()
                    .comments(comments)
                    .users(user)
                    .build();
            commentLikeRepository.save(commentLike);
            comments.setLikeCount(comments.getLikeCount() + 1);
        }
        commentRepository.save(comments);

        return LikeResponseDTO.builder()
                .likeCount(comments.getLikeCount())
                .liked(!isLiked)
                .build();
    }
}

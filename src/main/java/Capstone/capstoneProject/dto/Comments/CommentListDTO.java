package Capstone.capstoneProject.dto.Comments;

import Capstone.capstoneProject.dto.UserSummaryDTO;
import Capstone.capstoneProject.entity.Boards.Boards;
import Capstone.capstoneProject.entity.Comments;
import Capstone.capstoneProject.entity.Users.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CommentListDTO {
    private Long id;
    private UserSummaryDTO user;
    private String content;
    private int likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isLiked; // 내가 좋아요 눌렀는지 여부
    private Long parentId;
    private List<CommentListDTO> replies;

    public static CommentListDTO from(CommentListDTO commentListDTO, boolean isLiked) {
        return CommentListDTO.builder()
                .id(commentListDTO.getId())
                .user(commentListDTO.getUser())
                .content(commentListDTO.getContent())
                .likeCount(commentListDTO.getLikeCount())
                .createdAt(commentListDTO.getCreatedAt())
                .updatedAt(commentListDTO.getUpdatedAt())
                .isLiked(isLiked)
                .parentId(commentListDTO.getParentId() != null ? commentListDTO.getParentId() : null)
                .replies(new ArrayList<>(commentListDTO.getReplies() != null ? commentListDTO.getReplies() : List.of()))
                .build();
    }



    public static List<CommentListDTO> fromList(Boards boards, Users currentUser) {
        List<CommentListDTO> allComments = boards.getCommentsList().stream()
                .map(comment -> {
                    // 현재 유저가 좋아요 눌렀는지 여부 체크
                    boolean isLiked = comment.getCommentLikesList().stream()
                            .anyMatch(like -> like.getUsers().equals(currentUser));

                    return CommentListDTO.builder()
                            .id(comment.getId())
                            .user(UserSummaryDTO.from(comment.getUsers()))
                            .content(comment.getContent())
                            .likeCount(comment.getCommentLikesList().size())
                            .createdAt(comment.getCreatedAt())
                            .updatedAt(comment.getUpdatedAt())
                            .isLiked(isLiked)
                            .parentId(comment.getParentId() != null ? comment.getParentId().getId() : null)
                            .replies(new ArrayList<>())
                            .build();
                })
                .toList();


        Map<Long, CommentListDTO> map = allComments.stream()
                .collect(Collectors.toMap(CommentListDTO::getId, c -> c));

        List<CommentListDTO> rootComments = new ArrayList<>();

        for (CommentListDTO dto : allComments) {
            if (dto.getParentId() == null) {
                rootComments.add(dto); // 일반 댓글
            } else {
                CommentListDTO parent = map.get(dto.getParentId());
                if (parent != null) {
                    parent.getReplies().add(dto); // 부모 DTO에 대댓글 추가
                }
            }
        }

        return rootComments;
    }

    public static List<CommentListDTO> fromComments(List<Comments> comments, Users currentUser) {
        List<CommentListDTO> allComments = comments.stream()
                .map(comment -> {

                    boolean isLiked = comment.getCommentLikesList().stream()
                            .anyMatch(like -> like.getUsers().equals(currentUser));

                    return CommentListDTO.builder()
                            .id(comment.getId())
                            .user(UserSummaryDTO.from(comment.getUsers()))
                            .content(comment.getContent())
                            .likeCount(comment.getCommentLikesList().size())
                            .createdAt(comment.getCreatedAt())
                            .updatedAt(comment.getUpdatedAt())
                            .isLiked(isLiked)
                            .parentId(comment.getParentId() != null ? comment.getParentId().getId() : null)
                            .replies(new ArrayList<>())
                            .build();

                }).toList();

        Map<Long, CommentListDTO> map = allComments.stream()
                .collect(Collectors.toMap(CommentListDTO::getId, c -> c));

        List<CommentListDTO> rootComments = new ArrayList<>();

        for (CommentListDTO dto : allComments) {
            if (dto.getParentId() == null) {
                rootComments.add(dto);
            } else {
                CommentListDTO parent = map.get(dto.getParentId());
                if (parent != null) {
                    parent.getReplies().add(dto);
                }
            }
        }

        return rootComments;
    }



}

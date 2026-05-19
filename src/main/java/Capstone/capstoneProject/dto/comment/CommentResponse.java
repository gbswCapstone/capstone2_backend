package Capstone.capstoneProject.dto.comment;

import Capstone.capstoneProject.dto.user.UserSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CommentResponse {
    private Long id;
    private UserSummaryDTO users;
    private Long parentId;
    private String content;
    private int likeCount;
    private boolean isLiked;
    private LocalDateTime updatedAt;
}

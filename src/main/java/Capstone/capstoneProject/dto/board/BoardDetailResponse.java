package Capstone.capstoneProject.dto.board;

import Capstone.capstoneProject.dto.user.UserSummaryDTO;
import Capstone.capstoneProject.entity.board.Boards;
import Capstone.capstoneProject.entity.user.Users;
import Capstone.capstoneProject.enums.BoardCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class BoardDetailResponse {
    private Long id;
    private UserSummaryDTO user;
    private String title;
    private BoardCategory category;
    private String content;
    private BoardLikesDTO likes;
    private int commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<BoardImageListDTO> imagesList;
    private List<BoardVideoListDTO> videosList;

    public static BoardDetailResponse from(
            Boards boards, Users user, int likeCount, boolean isLiked) {

        return BoardDetailResponse.builder()
                .id(boards.getId())
                .user(UserSummaryDTO.from(boards.getUsers()))
                .title(boards.getTitle())
                .category(boards.getCategory())
                .content(boards.getContent())
                .likes(BoardLikesDTO.from(boards, user, likeCount, isLiked))
                .commentCount(boards.getCommentCount())
                .imagesList(BoardImageListDTO.fromList(boards))
                .videosList(BoardVideoListDTO.fromList(boards))
                .build();

    }
}

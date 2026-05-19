package Capstone.capstoneProject.dto.board;

import Capstone.capstoneProject.dto.user.UserSummaryDTO;
import Capstone.capstoneProject.entity.board.Boards;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class BoardDeleteResponse {
    private String title;
    private UserSummaryDTO user;
    private String image;
    private LocalDateTime deletedAt;

    public static BoardDeleteResponse from(Boards boards) {
        return BoardDeleteResponse.builder()
                .title(boards.getTitle())
                .user(UserSummaryDTO.from(boards.getUsers()))
                .image(BoardImageDTO.from(boards))
                .deletedAt(boards.getDeletedAt())
                .build();
    }
}

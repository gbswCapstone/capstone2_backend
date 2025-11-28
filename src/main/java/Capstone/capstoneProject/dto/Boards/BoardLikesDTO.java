package Capstone.capstoneProject.dto.Boards;

import Capstone.capstoneProject.entity.Boards.Boards;
import Capstone.capstoneProject.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class BoardLikesDTO {
    private int likeCount;
    private boolean isLiked;

    public static BoardLikesDTO from(Boards board, Users users, int likeCount, boolean isLiked) {
        return BoardLikesDTO.builder()
                .likeCount(likeCount)
                .isLiked(isLiked)
                .build();
    }
}

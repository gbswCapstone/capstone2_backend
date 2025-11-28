package Capstone.capstoneProject.dto.Boards;

import Capstone.capstoneProject.entity.Boards.BoardImages;
import Capstone.capstoneProject.entity.Boards.Boards;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class BoardImageDTO {

    private String imageUrl;

    public static String from(Boards boards) {
        List<BoardImages> boardImages = boards.getBoardImages();
        if (boardImages == null || boardImages.isEmpty()) {
            return null;
        }

        BoardImages firstImage = boardImages.get(0);

        return firstImage.getImageUrl();
    }
}

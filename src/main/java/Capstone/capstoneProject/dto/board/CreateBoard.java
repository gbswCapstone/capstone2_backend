package Capstone.capstoneProject.dto.board;

import Capstone.capstoneProject.entity.board.BoardImages;
import Capstone.capstoneProject.entity.board.BoardVideos;
import Capstone.capstoneProject.enums.BoardCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CreateBoard {
    private String title;
    private String content;
    private BoardCategory category;
    private List<BoardImages> imagesList;
    private List<BoardVideos> videosList;
}

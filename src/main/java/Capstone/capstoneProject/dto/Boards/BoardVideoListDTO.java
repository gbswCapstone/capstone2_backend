package Capstone.capstoneProject.dto.Boards;

import Capstone.capstoneProject.entity.Boards.BoardImages;
import Capstone.capstoneProject.entity.Boards.BoardVideos;
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
public class BoardVideoListDTO {
    private Long id;
    private String videoUrl;

    public static BoardVideoListDTO from(BoardVideos boardVideos) {
        return BoardVideoListDTO.builder()
                .id(boardVideos.getId())
                .videoUrl(boardVideos.getVideoUrl())
                .build();
    }

    public static List<BoardVideoListDTO> fromList(Boards boards) {
        return boards.getBoardVideos().stream()
                .map(BoardVideoListDTO::from)
                .toList();
    }
}

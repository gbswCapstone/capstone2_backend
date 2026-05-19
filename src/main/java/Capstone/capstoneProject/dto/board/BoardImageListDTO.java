package Capstone.capstoneProject.dto.board;

import Capstone.capstoneProject.entity.board.BoardImages;
import Capstone.capstoneProject.entity.board.Boards;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class BoardImageListDTO {
    private Long id;
    private String imageUrl;

    public static BoardImageListDTO from(BoardImages boardImage) {
        return BoardImageListDTO.builder()
                .id(boardImage.getId())
                .imageUrl(boardImage.getImageUrl())
                .build();
    }

    public static List<BoardImageListDTO> fromList(Boards boards) {
        return boards.getBoardImages().stream()
                .map(BoardImageListDTO::from) // 이제 BoardImages를 받는 from 사용
                .toList();
    }

}

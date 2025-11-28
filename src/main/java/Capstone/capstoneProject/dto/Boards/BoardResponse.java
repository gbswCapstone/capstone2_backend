package Capstone.capstoneProject.dto.Boards;

import Capstone.capstoneProject.dto.UserSummaryDTO;
import Capstone.capstoneProject.entity.Boards.BoardImages;
import Capstone.capstoneProject.entity.Boards.BoardVideos;
import Capstone.capstoneProject.entity.Boards.Boards;
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
public class BoardResponse {
    private Long id;
    private UserSummaryDTO user;
    private String title;
    private BoardCategory category;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String image; // 대표 이미지 한개만

    public static BoardResponse from(Boards boards) {
        return BoardResponse.builder()
                .id(boards.getId())
                .user(UserSummaryDTO.from(boards.getUsers()))
                .title(boards.getTitle())
                .category(boards.getCategory())
                .content(boards.getContent())
                .createdAt(boards.getCreatedAt())
                .updatedAt(boards.getUpdatedAt())
                .image(BoardImageDTO.from(boards))
                .build();
    }
}

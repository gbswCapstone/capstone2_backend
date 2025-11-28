package Capstone.capstoneProject.dto.Boards;

import Capstone.capstoneProject.dto.UserSummaryDTO;
import Capstone.capstoneProject.entity.Boards.Boards;
import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.enums.BoardCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@Builder
public class BoardListDTO {
    private Long id;
    private UserSummaryDTO user;
    private String title;
    private BoardCategory category;
    private String content;
    private BoardLikesDTO likes;
    private int commentCount;
    private LocalDateTime createdAt;
    private String image; // 대표 이미지 한개만
}

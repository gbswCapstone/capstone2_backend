package Capstone.capstoneProject.dto.Comments;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CreateComment {
    private Long parentId;
    private String content;
}

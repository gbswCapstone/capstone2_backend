package Capstone.capstoneProject.dto.challenge;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LikeResponseDTO {
    private boolean liked;
    private int likeCount;
}

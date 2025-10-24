package Capstone.capstoneProject.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
public class ChallengeLikeResponse {
    @JsonProperty("updated_like_count")
    int updatedLikeCount;
}



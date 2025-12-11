package Capstone.capstoneProject.dto.Challenges;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ChallengeJoinResponse {
    private String roomId;
}

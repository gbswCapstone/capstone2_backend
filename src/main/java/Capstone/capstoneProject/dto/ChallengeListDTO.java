package Capstone.capstoneProject.dto;

import Capstone.capstoneProject.entity.challenges.Challenges;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeListDTO {
    private Long id;
    private String title;
    private int maxPersonnel;
    private long currentPersonnel; // 현재 참여인원
    private int likeCount; // 좋아요수

    public ChallengeListDTO(Challenges challenge) {
        this.id = challenge.getId();
        this.title = challenge.getTitle();
        this.maxPersonnel = challenge.getMaxPersonnel();
        this.currentPersonnel = getCurrentPersonnel();
        this.likeCount = getLikeCount();
    }
}

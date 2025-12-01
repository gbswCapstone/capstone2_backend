package Capstone.capstoneProject.dto.Challenges;

import Capstone.capstoneProject.entity.challenges.ChallengeHashtag;
import Capstone.capstoneProject.entity.challenges.Challenges;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeListDTO {
    private Long id;
    private String title;
    private int maxPersonnel;
    private Long currentPersonnel; // 현재 참여인원
    private int likeCount; // 좋아요수
    private String goal; // 목표
    private List<String> hashtags;
    private String image; // 이미지

    public ChallengeListDTO(Challenges challenge) {
        this.id = challenge.getId();
        this.title = challenge.getTitle();
        this.maxPersonnel = challenge.getMaxPersonnel();
        this.currentPersonnel = 0L; // 임시값
        this.likeCount = getLikeCount();
        this.goal = challenge.getGoal();
        this.hashtags = challenge.getChallengeHashtags()
                .stream()
                .map(ch -> ch.getHashtag().getName())
                .collect(Collectors.toList());
        this.image = challenge.getImage();

    }
}

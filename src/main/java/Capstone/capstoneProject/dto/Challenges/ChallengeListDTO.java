package Capstone.capstoneProject.dto.Challenges;

import Capstone.capstoneProject.entity.challenges.Challenges;
import Capstone.capstoneProject.enums.UserJobs;
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
    private int currentPersonnel; // 현재 참여인원
    private int likeCount; // 좋아요수
    private String goal; // 목표
    private List<String> hashtags;
    private String image; // 이미지
    private boolean joined; // 가입 여부
    private String roomId; // 채팅방 아이디 (참여한 경우만 세팅 참여안한 경우 null)
    private UserJobs job;

    public ChallengeListDTO(Challenges challenge) {
        this.id = challenge.getId();
        this.title = challenge.getTitle();
        this.maxPersonnel = challenge.getMaxPersonnel();
        this.currentPersonnel = challenge.getChallengeUsers().size();
        this.likeCount = challenge.getLikeCount();
        this.goal = challenge.getGoal();
        this.hashtags = challenge.getChallengeHashtags()
                .stream()
                .map(ch -> ch.getHashtag().getName())
                .collect(Collectors.toList());
        this.image = challenge.getImage();
        this.job = challenge.getJob();
    }
}

package Capstone.capstoneProject.dto;

import Capstone.capstoneProject.entity.challenges.Challenges;
import Capstone.capstoneProject.enums.UserJobs;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ChallengeDetailResponse {
    private final Long id;

    @JsonProperty("created_by")
    private final UserResponseDTO createdBy;
    private final String title;
    @JsonProperty("max_personnel") // 인원 제한
    private int maxPersonnel;
    private String image;
    private UserJobs job;
    private String goal;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    private final List<String> hashtags;// 이것만 챌린지해시태그 엔티티

    public ChallengeDetailResponse(Long id,
                                   UserResponseDTO createdBy,
                                   String title,
                                   int maxPersonnel,
                                   String image,
                                   UserJobs job,
                                   String goal,
                                   LocalDateTime createdAt,
                                   List<String> hashtags) {
        this.id = id;
        this.createdBy = createdBy;
        this.title = title;
        this.maxPersonnel = maxPersonnel;
        this.image = image;
        this.job = job;
        this.goal = goal;
        this.createdAt = createdAt;
        this.hashtags = hashtags;
    }


    //엔티티 -> DTO 변환 편의 메서드
    public static ChallengeDetailResponse fromEntity(Challenges challenge) {
        List<String> hashtagNames = challenge.getChallengeHashtags()  // ChallengeHashtag Set 필요
                .stream()
                .map(ch -> ch.getHashtag().getName())
                .toList();

        return new ChallengeDetailResponse(
                challenge.getId(),
                new UserResponseDTO(challenge.getCreatedBy(), challenge.getCreatedBy().getProfile()), // UserResponseDTO 생성
                challenge.getTitle(),
                challenge.getMaxPersonnel(),
                challenge.getImage(),
                challenge.getJob(),
                challenge.getGoal(),
                challenge.getCreatedAt(),
                hashtagNames
        );
    }
}



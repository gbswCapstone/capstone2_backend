package Capstone.capstoneProject.dto.Challenges;

import Capstone.capstoneProject.dto.UserResponseDTO;
import Capstone.capstoneProject.entity.challenges.Challenges;
import Capstone.capstoneProject.enums.UserJobs;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ChallengeDetailResponse {
    private final Long id;

    private final UserResponseDTO createdBy;
    private final String title;
    private String image;
    private UserJobs job;
    private String goal;
    private LocalDateTime createdAt;
    private final List<String> hashtags;// 이것만 챌린지해시태그 엔티티
    private boolean isLiked; // 내가 좋아요를 눌렀는지
    private boolean joined; // 현재 로그인 유저가 참여했는지
    private String roomId; // 참여한 경우만 WebSocket 구독용
    private int likeCount;
    private int maxPersonnel;
    private int currentPersonnel; // 현재 참여인원

    public ChallengeDetailResponse(Long id,
                                   UserResponseDTO createdBy,
                                   String title,
                                   int maxPersonnel,
                                   String image,
                                   UserJobs job,
                                   String goal,
                                   LocalDateTime createdAt,
                                   boolean isLiked,
                                   List<String> hashtags, int likeCount,
                                   int currentPersonnel
                                   ) {
        this.id = id;
        this.createdBy = createdBy;
        this.title = title;
        this.maxPersonnel = maxPersonnel;
        this.image = image;
        this.job = job;
        this.goal = goal;
        this.createdAt = createdAt;
        this.hashtags = hashtags;
        this.isLiked = isLiked;
        this.joined = false; // 기본값 false
        this.roomId = null;   // 기본값 null
        this.likeCount = likeCount;
        this.currentPersonnel = currentPersonnel;
    }


    public static ChallengeDetailResponse fromEntity(Challenges challenge, boolean isLiked) {
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
                isLiked,
                hashtagNames,
                challenge.getLikeCount(),
                challenge.getChallengeUsers().size()
        );
    }

    public void setJoined(boolean joined) {
        this.joined = joined;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}



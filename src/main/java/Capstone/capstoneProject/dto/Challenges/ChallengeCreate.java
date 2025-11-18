package Capstone.capstoneProject.dto.Challenges;

import Capstone.capstoneProject.enums.UserJobs;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChallengeCreate {
    private String title; // 방 이름
    private String image; // 방 이미지
    private String goal; // 목표입력
    private UserJobs job; // 직업선택
    private List<String> hashtags; // 해시태그 입력
    @JsonProperty("max_personnel")
    private int maxPersonnel; // 방 정원 입력

}

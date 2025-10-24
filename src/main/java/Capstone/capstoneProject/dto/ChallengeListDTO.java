package Capstone.capstoneProject.dto;

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

}

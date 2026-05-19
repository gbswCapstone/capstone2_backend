package Capstone.capstoneProject.dto.user;


import Capstone.capstoneProject.entity.user.UserProfile;
import Capstone.capstoneProject.entity.user.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserSummaryDTO {
    private Long id;
    private String nickname;
    private String profileImg;
    private String statusMessage;

    public static UserSummaryDTO from(Users users) {
        UserProfile profile = users.getProfile();
        return UserSummaryDTO.builder()
                .id(users.getId())
                .nickname(profile.getNickname())
                .profileImg(profile.getProfileImg())
                .statusMessage(profile.getStatusMessage())
                .build();
    }
}

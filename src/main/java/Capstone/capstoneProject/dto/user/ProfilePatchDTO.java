package Capstone.capstoneProject.dto.user;


import Capstone.capstoneProject.entity.user.UserProfile;
import Capstone.capstoneProject.entity.user.Users;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfilePatchDTO {
    private String nickname;
    private String statusMessage;
    private String profileImg;

    public ProfilePatchDTO(Users user, UserProfile profile) {
        this.nickname = profile.getNickname();
        this.statusMessage = profile.getStatusMessage();
        this.profileImg = profile.getProfileImg();
    }
}

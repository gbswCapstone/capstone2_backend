package Capstone.capstoneProject.dto;

import Capstone.capstoneProject.entity.UserProfile;
import Capstone.capstoneProject.entity.Users;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfilePatchDTO {
    private String password;
    private String nickname;
    private String statusMessage;
    private String profileImg;

    public ProfilePatchDTO(Users user, UserProfile profile) {
        this.password = user.getPassword();
        this.nickname = profile.getNickname();
        this.statusMessage = profile.getStatusMessage();
        this.profileImg = profile.getProfileImg();
    }
}

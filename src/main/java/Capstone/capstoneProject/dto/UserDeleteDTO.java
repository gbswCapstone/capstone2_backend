package Capstone.capstoneProject.dto;

import Capstone.capstoneProject.entity.UserProfile;
import Capstone.capstoneProject.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class UserDeleteDTO {
    private final String email;
    private final String nickname;
    private final String profileImg;
    private final LocalDateTime deletedAt;

    public UserDeleteDTO(Users user, UserProfile profile) {
        this.email = user.getEmail();
        this.nickname = profile.getNickname();
        this.profileImg = profile.getProfileImg();
        this.deletedAt = user.getDeletedAt();
    }
}

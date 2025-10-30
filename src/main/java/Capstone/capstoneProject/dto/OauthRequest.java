package Capstone.capstoneProject.dto;


import Capstone.capstoneProject.entity.UserProfile;
import Capstone.capstoneProject.entity.Users;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OauthRequest {
    private String email;
    private String nickname;
}

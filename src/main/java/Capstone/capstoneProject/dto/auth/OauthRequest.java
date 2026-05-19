package Capstone.capstoneProject.dto.auth;


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

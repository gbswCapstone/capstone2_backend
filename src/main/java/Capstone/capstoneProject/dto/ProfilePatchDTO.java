package Capstone.capstoneProject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfilePatchDTO {
    private String nickname;
    private String password;
    private String passwordCheck;
    private String statusMessage;
    private String profileImg;
}

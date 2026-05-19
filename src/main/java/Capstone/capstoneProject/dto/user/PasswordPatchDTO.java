package Capstone.capstoneProject.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PasswordPatchDTO {
    private String password;
    private String newPassword;
}

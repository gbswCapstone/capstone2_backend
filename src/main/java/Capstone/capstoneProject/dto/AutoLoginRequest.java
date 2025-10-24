package Capstone.capstoneProject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AutoLoginRequest {
    private String refreshToken;
}

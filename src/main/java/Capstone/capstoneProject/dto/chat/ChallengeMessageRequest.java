package Capstone.capstoneProject.dto.chat;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ChallengeMessageRequest {

    @NotBlank(message = "roomId를 입력해주세요.")
    private String roomId;

    @NotBlank(message = "메시지를 입력해주세요.")
    private String message;
}

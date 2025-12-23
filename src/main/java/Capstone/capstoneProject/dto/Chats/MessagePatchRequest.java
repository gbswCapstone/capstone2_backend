package Capstone.capstoneProject.dto.Chats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MessagePatchRequest {
    private String message;
}

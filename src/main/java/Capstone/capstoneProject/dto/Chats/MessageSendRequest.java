package Capstone.capstoneProject.dto.Chats;

import Capstone.capstoneProject.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MessageSendRequest {
    private String content;
    private String roomId;
    private MessageType messageType;
}

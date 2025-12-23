package Capstone.capstoneProject.exceptions.forbidden;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class ChatBotRoomAccessDeniedException extends DomainException {
    public ChatBotRoomAccessDeniedException() { super(HttpStatus.FORBIDDEN, "해당 챗봇 채팅방 유저가 아닙니다."); }

    public ChatBotRoomAccessDeniedException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}

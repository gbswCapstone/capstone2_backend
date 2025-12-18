package Capstone.capstoneProject.exceptions.forbidden;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class ChatRoomAccessDeniedException extends DomainException {
    public ChatRoomAccessDeniedException() { super(HttpStatus.FORBIDDEN, "해당 채팅방 유저가 아닙니다."); }

    public ChatRoomAccessDeniedException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}

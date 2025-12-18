package Capstone.capstoneProject.exceptions.forbidden;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class ChatRoomHostRequiredException extends DomainException {

    public ChatRoomHostRequiredException() { super(HttpStatus.FORBIDDEN, "채팅방 방장만 가능한 권한입니다."); }

    public ChatRoomHostRequiredException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}


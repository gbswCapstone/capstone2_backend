package Capstone.capstoneProject.exceptions.notfound;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class ChatRoomMessageNotFoundException extends DomainException {
    public ChatRoomMessageNotFoundException() { super(HttpStatus.NOT_FOUND, "채팅방의 해당 메시지를 찾을 수 없습니다."); }

    public ChatRoomMessageNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}

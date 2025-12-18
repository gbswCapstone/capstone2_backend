package Capstone.capstoneProject.exceptions.notfound;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class ChatRoomNotFoundException extends DomainException {
    public ChatRoomNotFoundException() { super(HttpStatus.NOT_FOUND, "해당 채팅방을 찾을 수 없습니다."); }

    public ChatRoomNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}

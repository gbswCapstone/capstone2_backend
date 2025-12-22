package Capstone.capstoneProject.exceptions.conflict;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class ChatBotRoomAlreadyExistsException extends DomainException {

    public ChatBotRoomAlreadyExistsException() { super(HttpStatus.CONFLICT, "이미 챗봇 채팅방이 존재합니다."); }

    public ChatBotRoomAlreadyExistsException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}

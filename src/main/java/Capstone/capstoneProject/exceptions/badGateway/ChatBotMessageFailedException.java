package Capstone.capstoneProject.exceptions.badGateway;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class ChatBotMessageFailedException extends DomainException {
    public ChatBotMessageFailedException() { super(HttpStatus.BAD_GATEWAY, "챗봇 메시지 생성에 실패했습니다."); }

    public ChatBotMessageFailedException(String message) {
        super(HttpStatus.BAD_GATEWAY, message);
    }
}

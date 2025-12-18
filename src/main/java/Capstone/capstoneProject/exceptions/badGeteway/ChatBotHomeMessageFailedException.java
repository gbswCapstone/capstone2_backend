package Capstone.capstoneProject.exceptions.badGeteway;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class ChatBotHomeMessageFailedException extends DomainException {
    public ChatBotHomeMessageFailedException() { super(HttpStatus.BAD_GATEWAY, "홈 챗봇 메시지 생성에 실패했습니다."); }

    public ChatBotHomeMessageFailedException(String message) {
        super(HttpStatus.BAD_GATEWAY, message);
    }
}

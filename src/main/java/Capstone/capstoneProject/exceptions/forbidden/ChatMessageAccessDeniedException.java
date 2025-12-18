package Capstone.capstoneProject.exceptions.forbidden;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class ChatMessageAccessDeniedException extends DomainException {
    public ChatMessageAccessDeniedException() { super(HttpStatus.FORBIDDEN, "채팅방의 해당 메시지에 관한 권한이 없습니다."); }

    public ChatMessageAccessDeniedException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}

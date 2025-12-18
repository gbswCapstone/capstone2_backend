package Capstone.capstoneProject.exceptions.forbidden;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class ChatImageAccessDeniedException extends DomainException {
    public ChatImageAccessDeniedException() { super(HttpStatus.FORBIDDEN, "채팅방의 해당 이미지에 관한 권한이 없습니다."); }

    public ChatImageAccessDeniedException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}

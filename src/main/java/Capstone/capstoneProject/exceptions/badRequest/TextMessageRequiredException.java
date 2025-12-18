package Capstone.capstoneProject.exceptions.badRequest;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class TextMessageRequiredException extends DomainException {
    public TextMessageRequiredException() { super(HttpStatus.BAD_REQUEST, "텍스트 메시지가 아닙니다."); }

    public TextMessageRequiredException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}

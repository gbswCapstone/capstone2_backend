package Capstone.capstoneProject.exceptions.badRequest;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class ImageMessageRequiredException extends DomainException {
    public ImageMessageRequiredException() { super(HttpStatus.BAD_REQUEST, "이미지 메시지가 아닙니다."); }

    public ImageMessageRequiredException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}

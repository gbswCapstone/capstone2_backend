package Capstone.capstoneProject.exceptions.unauthorized;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class NotAuthenticatedException extends DomainException {
    public NotAuthenticatedException() { super(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."); }

    public NotAuthenticatedException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}

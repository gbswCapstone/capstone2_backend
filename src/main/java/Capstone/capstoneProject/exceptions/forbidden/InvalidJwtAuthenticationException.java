package Capstone.capstoneProject.exceptions.forbidden;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class InvalidJwtAuthenticationException extends DomainException {
    public InvalidJwtAuthenticationException() { super(HttpStatus.FORBIDDEN, "jwt 토큰이 없거나 유효하지 않습니다."); }

    public InvalidJwtAuthenticationException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}

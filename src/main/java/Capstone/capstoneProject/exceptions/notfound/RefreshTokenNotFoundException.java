package Capstone.capstoneProject.exceptions.notfound;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class RefreshTokenNotFoundException extends DomainException {
    public RefreshTokenNotFoundException() { super(HttpStatus.FORBIDDEN, "유효하지 않은 리프레시 토큰입니다."); }

    public RefreshTokenNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}

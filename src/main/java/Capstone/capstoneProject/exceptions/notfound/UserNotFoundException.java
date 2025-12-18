package Capstone.capstoneProject.exceptions.notfound;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends DomainException {
    public UserNotFoundException() {super(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");}

    public UserNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}

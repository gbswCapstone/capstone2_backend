package Capstone.capstoneProject.exceptions.badRequest;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class InvalidQuantityException extends DomainException {
    public InvalidQuantityException() {
        super(HttpStatus.BAD_REQUEST, "수량은 최소 1개 이상이어야 합니다.");
    }

    public InvalidQuantityException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}

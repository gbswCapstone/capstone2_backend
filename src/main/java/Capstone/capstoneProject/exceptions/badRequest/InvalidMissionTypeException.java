package Capstone.capstoneProject.exceptions.badRequest;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class InvalidMissionTypeException extends DomainException {
    public InvalidMissionTypeException() {
        super(HttpStatus.BAD_REQUEST, "잘못된 미션 타입 입니다.");
    }

    public InvalidMissionTypeException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}

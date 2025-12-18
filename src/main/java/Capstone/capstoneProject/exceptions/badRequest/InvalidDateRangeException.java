package Capstone.capstoneProject.exceptions.badRequest;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class InvalidDateRangeException extends DomainException {
    public InvalidDateRangeException() { super(HttpStatus.BAD_REQUEST, "시작 날짜는 종료 날짜보다 늦을 수 없습니다."); }

    public InvalidDateRangeException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}

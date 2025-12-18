package Capstone.capstoneProject.exceptions.badRequest;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class WeekNotInMonthException extends DomainException {
    public WeekNotInMonthException() { super(HttpStatus.BAD_REQUEST, "해당 월에 요청하는 주차가 없습니다."); }

    public WeekNotInMonthException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}

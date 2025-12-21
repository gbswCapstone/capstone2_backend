package Capstone.capstoneProject.exceptions.badRequest;


import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class UsageShareMessageRequiredException extends DomainException {

    public UsageShareMessageRequiredException() {
        super(HttpStatus.BAD_REQUEST, "사용내역 메시지가 아닙니다.");
    }

    public UsageShareMessageRequiredException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}


package Capstone.capstoneProject.exceptions.notfound;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class ChallengeNotFoundException extends DomainException {

    public ChallengeNotFoundException() { super(HttpStatus.NOT_FOUND, "해당 챌린지방을 찾을 수 없습니다."); }

    public ChallengeNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}

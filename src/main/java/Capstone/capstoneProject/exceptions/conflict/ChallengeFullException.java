package Capstone.capstoneProject.exceptions.conflict;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class ChallengeFullException extends DomainException {
    public ChallengeFullException() { super(HttpStatus.CONFLICT, "참여 인원이 가득 찼습니다."); }

    public ChallengeFullException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}

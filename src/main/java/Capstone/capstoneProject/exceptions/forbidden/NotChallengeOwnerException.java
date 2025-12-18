package Capstone.capstoneProject.exceptions.forbidden;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class NotChallengeOwnerException extends DomainException {
    public NotChallengeOwnerException() { super(HttpStatus.FORBIDDEN, "해당 챌린지방에 관한 권한이 없습니다."); }

    public NotChallengeOwnerException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}

package Capstone.capstoneProject.exceptions.forbidden;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class ChallengeAccessDeniedException extends DomainException {
    public ChallengeAccessDeniedException() { super(HttpStatus.FORBIDDEN, "챌린지방 참가자가 아닙니다."); }

    public ChallengeAccessDeniedException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}



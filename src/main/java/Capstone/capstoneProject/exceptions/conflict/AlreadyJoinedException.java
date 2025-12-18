package Capstone.capstoneProject.exceptions.conflict;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class AlreadyJoinedException extends DomainException {

    public AlreadyJoinedException() { super(HttpStatus.CONFLICT, "이미 참여한 챌린지입니다."); }

    public AlreadyJoinedException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}

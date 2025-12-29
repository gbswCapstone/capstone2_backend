package Capstone.capstoneProject.exceptions.forbidden;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class MissionAccessDeniedException extends DomainException {
    public MissionAccessDeniedException() { super(HttpStatus.FORBIDDEN, "해당 미션에 관한 권한이 없습니다."); }

    public MissionAccessDeniedException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}



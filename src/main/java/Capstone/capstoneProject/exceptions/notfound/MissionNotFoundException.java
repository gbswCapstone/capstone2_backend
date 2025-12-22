package Capstone.capstoneProject.exceptions.notfound;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class MissionNotFoundException extends DomainException {

    public MissionNotFoundException() { super(HttpStatus.NOT_FOUND, "해당 미션을 찾을 수 없습니다."); }

    public MissionNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}

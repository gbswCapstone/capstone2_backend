package Capstone.capstoneProject.exceptions.notfound;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class UserCharacterNotFoundException extends DomainException {
    public UserCharacterNotFoundException() { super(HttpStatus.NOT_FOUND, "해당 유저의 캐릭터(등급)을 찾을 수 없습니다."); }

    public UserCharacterNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}

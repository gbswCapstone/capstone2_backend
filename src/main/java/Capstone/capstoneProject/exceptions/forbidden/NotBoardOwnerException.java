package Capstone.capstoneProject.exceptions.forbidden;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class NotBoardOwnerException extends DomainException {
    public NotBoardOwnerException() { super(HttpStatus.FORBIDDEN, "해당 게시글에 관한 권한이 없습니다."); }

    public NotBoardOwnerException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}

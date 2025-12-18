package Capstone.capstoneProject.exceptions.forbidden;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class NotCommentOwnerException extends DomainException {
    public NotCommentOwnerException() { super(HttpStatus.FORBIDDEN, "해당 댓글에 관한 권한이 없습니다."); }

    public NotCommentOwnerException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}

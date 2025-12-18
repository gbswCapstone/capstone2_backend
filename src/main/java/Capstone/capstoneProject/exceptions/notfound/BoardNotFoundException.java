package Capstone.capstoneProject.exceptions.notfound;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class BoardNotFoundException extends DomainException {

    public BoardNotFoundException() { super(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다."); }

    public BoardNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }


}

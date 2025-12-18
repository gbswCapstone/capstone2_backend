package Capstone.capstoneProject.exceptions.notfound;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class BoardLikeNotFoundException extends DomainException {
    public BoardLikeNotFoundException() { super(HttpStatus.NOT_FOUND, "해당 게시글의 좋아요 데이터가 존재하지 않습니다."); }

    public BoardLikeNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

}

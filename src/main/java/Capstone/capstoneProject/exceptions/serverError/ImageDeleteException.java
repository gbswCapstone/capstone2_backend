package Capstone.capstoneProject.exceptions.serverError;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class ImageDeleteException extends DomainException {
    public ImageDeleteException() { super(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 삭제에 실패했습니다."); }

    public ImageDeleteException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}

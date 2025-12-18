package Capstone.capstoneProject.exceptions.serverError;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class ReceiptAiServerException extends DomainException {
    public ReceiptAiServerException() { super(HttpStatus.INTERNAL_SERVER_ERROR, "영수증 AI 호출에 실패했습니다."); }

    public ReceiptAiServerException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}

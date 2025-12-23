package Capstone.capstoneProject.exceptions.conflict;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class BalanceAlreadyExistsException extends DomainException {
    public BalanceAlreadyExistsException() { super(HttpStatus.CONFLICT, "잔액 정보가 이미 존재합니다."); }

    public BalanceAlreadyExistsException(String message) {
        super(HttpStatus.CONFLICT, message);
    }

}

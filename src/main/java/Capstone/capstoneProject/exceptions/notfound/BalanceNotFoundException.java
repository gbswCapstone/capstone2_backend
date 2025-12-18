package Capstone.capstoneProject.exceptions.notfound;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class BalanceNotFoundException extends DomainException {
    public BalanceNotFoundException() { super(HttpStatus.NOT_FOUND, "잔액을 찾을 수 없습니다."); }

    public BalanceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}

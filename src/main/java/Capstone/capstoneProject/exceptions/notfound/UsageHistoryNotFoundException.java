package Capstone.capstoneProject.exceptions.notfound;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class UsageHistoryNotFoundException extends DomainException {
    public UsageHistoryNotFoundException() { super(HttpStatus.NOT_FOUND, "해당 사용내역을 찾을 수 없습니다."); }

    public UsageHistoryNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}

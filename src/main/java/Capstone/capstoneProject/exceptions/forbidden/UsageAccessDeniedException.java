package Capstone.capstoneProject.exceptions.forbidden;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class UsageAccessDeniedException extends DomainException {
    public UsageAccessDeniedException() { super(HttpStatus.FORBIDDEN, "본인 사용내역만 공유가능합니다."); }

    public UsageAccessDeniedException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}

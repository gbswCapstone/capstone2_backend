package Capstone.capstoneProject.exceptions.badRequest;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class ConflictingSearchCriteriaException extends DomainException {
    public ConflictingSearchCriteriaException() {
            super(HttpStatus.BAD_REQUEST, "검색 조건(Preset, 커스텀 날짜, 연/월/주)은 하나만 선택해야 합니다.");
    }

    public ConflictingSearchCriteriaException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}

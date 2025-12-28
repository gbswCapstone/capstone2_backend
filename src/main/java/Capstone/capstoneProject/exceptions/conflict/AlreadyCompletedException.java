package Capstone.capstoneProject.exceptions.conflict;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class AlreadyCompletedException extends DomainException {

  public AlreadyCompletedException() { super(HttpStatus.CONFLICT, "이미 완료한 상태입니다."); }

  public AlreadyCompletedException(String message) {
    super(HttpStatus.CONFLICT, message);
  }

}

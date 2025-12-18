package Capstone.capstoneProject.exceptions.conflict;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends DomainException {
  public UserAlreadyExistsException() { super(HttpStatus.CONFLICT, "이미 존재하는 계정입니다."); }

  public UserAlreadyExistsException(String message) {
    super(HttpStatus.CONFLICT, message);
  }
}

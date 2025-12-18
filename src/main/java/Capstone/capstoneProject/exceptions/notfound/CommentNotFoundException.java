package Capstone.capstoneProject.exceptions.notfound;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class CommentNotFoundException extends DomainException {
  public CommentNotFoundException() { super(HttpStatus.NOT_FOUND, "해당 댓글을 찾을 수 없습니다."); }

  public CommentNotFoundException(String message) {
    super(HttpStatus.NOT_FOUND, message);
  }
}

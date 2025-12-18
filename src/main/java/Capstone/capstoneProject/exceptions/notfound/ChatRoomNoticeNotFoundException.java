package Capstone.capstoneProject.exceptions.notfound;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class ChatRoomNoticeNotFoundException extends DomainException {
  public ChatRoomNoticeNotFoundException() { super(HttpStatus.NOT_FOUND, "채팅방의 해당 공지를 찾을 수 없습니다."); }

  public ChatRoomNoticeNotFoundException(String message) {
    super(HttpStatus.NOT_FOUND, message);
  }
}


package Capstone.capstoneProject.exceptions.notfound;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class ChatBotRoomNotFoundException extends DomainException {
  public ChatBotRoomNotFoundException() { super(HttpStatus.NOT_FOUND, "해당 챗봇 채팅방을 찾을 수 없습니다."); }

  public ChatBotRoomNotFoundException(String message) {
    super(HttpStatus.NOT_FOUND, message);
  }
}

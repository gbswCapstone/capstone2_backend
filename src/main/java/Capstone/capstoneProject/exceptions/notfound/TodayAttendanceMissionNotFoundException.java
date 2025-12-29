package Capstone.capstoneProject.exceptions.notfound;

import Capstone.capstoneProject.exceptions.common.DomainException;
import org.springframework.http.HttpStatus;

public class TodayAttendanceMissionNotFoundException extends DomainException {
  public TodayAttendanceMissionNotFoundException() { super(HttpStatus.NOT_FOUND, "오늘의 출석 미션이 존재하지 않습니다."); }


  public TodayAttendanceMissionNotFoundException(String message) {
    super(HttpStatus.NOT_FOUND, message);
  }

}

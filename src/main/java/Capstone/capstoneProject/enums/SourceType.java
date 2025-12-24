package Capstone.capstoneProject.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SourceType {

    MISSION_COMPLETE("미션 완료"),
    ATTENDANCE_REWARD("출석 체크 보상"),
    SAVING_RATE_REWARD("절약률 달성 보상");

    private final String description;
}

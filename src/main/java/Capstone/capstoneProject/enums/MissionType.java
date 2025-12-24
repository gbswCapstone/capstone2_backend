package Capstone.capstoneProject.enums;

import lombok.Getter;

@Getter
public enum MissionType {
    AMOUNT,       // 금액 기준
    COUNT,        // 횟수 기준
    NO_EXPENSE,   // 무지출
    ATTENDANCE    // 출석
}

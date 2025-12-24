package Capstone.capstoneProject.enums;

import lombok.Getter;

@Getter
public enum MissionType {
    SPENDING_COUNT, // 지출 횟수 제한
    INCOME_GOAL, // 수입 목표 금액
    OUTLAY_GOAL, // 지출 목표 금액
    MONTHLY_OUTLAY_GOAL, // 이번달 목표 지출
    ATTENDANCE_CHECK, // 출석 체크 미션
    NO_OUTLAY // 무지출
}

package Capstone.capstoneProject.enums;

import lombok.Getter;

@Getter
public enum MessageType {
    TEXT,          // 일반 텍스트
    IMAGE,         // 사진 메시지
    MISSION,       // 미션  메시지
    NOTICE,        // 공지 메시지
    EXPENSE_SHARE, // 지출 공유 메시지
    SYSTEM         // 시스템 메시지 (입장/퇴장)
}

package Capstone.capstoneProject.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class MissionEventListener {

    private final MissionProgressService missionProgressService;

    // 사용 내역 미션
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleTransactionEvent(MissionCompletedEvent event) {
        missionProgressService.checkMissionProgress(event.getUserId());
    }

    // 출석
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleAttendanceEvent(AttendanceEvent event) {
        missionProgressService.checkAttendance(event.getUserId());
    }
}

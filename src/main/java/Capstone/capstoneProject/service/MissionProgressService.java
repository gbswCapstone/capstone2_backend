package Capstone.capstoneProject.service;

import Capstone.capstoneProject.entity.Missions.Missions;
import Capstone.capstoneProject.entity.Missions.UserMissions;
import Capstone.capstoneProject.enums.HistoryType;
import Capstone.capstoneProject.enums.MissionStatusType;
import Capstone.capstoneProject.enums.MissionType;
import Capstone.capstoneProject.enums.SourceType;
import Capstone.capstoneProject.repository.MissionRepository;
import Capstone.capstoneProject.repository.UsageHistoryRepository;
import Capstone.capstoneProject.repository.UserMissionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MissionProgressService {
    private final UserMissionRepository userMissionRepository;
    private final MissionRepository missionRepository;
    private final UsageHistoryRepository usageHistoryRepository;
    private final ExperienceService experienceService;

    public void checkMissionProgress(Long userId) {
        List<UserMissions> activeMissions =
                userMissionRepository.findActiveMissions(userId, LocalDate.now());

        for (UserMissions userMission : activeMissions) {
            evaluateMission(userMission);
        }
    }

    // 출석
    @Transactional
    public void checkAttendance(Long userId) {
        LocalDate today = LocalDate.now();

        List<UserMissions> attendanceMissions =
                userMissionRepository.findTodayAttendanceMissions(userId, today);

        for (UserMissions um : attendanceMissions) {

            // 이미 오늘 출석 완료한 경우 스킵
            if (um.getCompletedAt() != null &&
                    um.getCompletedAt().toLocalDate().isEqual(today)) {
                continue;
            }

           evaluateMission(um);
        }
    }

    private void evaluateMission(UserMissions userMission) {
        Missions mission = userMission.getMissions();

        switch (mission.getMissionType()) {
            case SPENDING_COUNT -> checkCountMission(userMission);

            case INCOME_GOAL, OUTLAY_GOAL, MONTHLY_OUTLAY_GOAL ->
                    checkAmountMission(userMission);

            case NO_OUTLAY -> checkNoExpenseMission(userMission);

            case ATTENDANCE_CHECK -> checkAttendanceMission(userMission);
        }
    }

    private void checkAmountMission(UserMissions um) {
        Missions m = um.getMissions();

        BigDecimal sum = usageHistoryRepository.sumAmount(
                um.getUsers().getId(),
                m.getCategory(),
                m.getStartDate(),
                m.getEndDate(),
                m.getMissionType() == MissionType.INCOME_GOAL
                        ? HistoryType.INCOME
                        : HistoryType.OUTLAY
        );

        boolean success =
                m.getMissionType() == MissionType.INCOME_GOAL
                        ? sum.compareTo(m.getGoalAmount()) >= 0
                        : sum.compareTo(m.getGoalAmount()) <= 0;

        if (success) {
            completeMission(um);
        }
    }

    // 무지출
    private void checkNoExpenseMission(UserMissions um) {
        Missions m = um.getMissions();

        // 아직 기간 종료 전이면 판별 X
        if (LocalDate.now().isBefore(m.getEndDate())) {
            return;
        }

        BigDecimal totalSpending =
                usageHistoryRepository.calculateTotalSpending(
                        um.getUsers(),
                        m.getCategory(),
                        m.getStartDate(),
                        m.getEndDate()
                );

        // 무지출 = 총 지출 금액이 0
        if (totalSpending.compareTo(BigDecimal.ZERO) == 0) {
            completeMission(um);
        }
    }

    private void completeMission(UserMissions um) {
        if (um.getMissionStatusType() == MissionStatusType.COMPLETED) return;

        um.setMissionStatusType(MissionStatusType.COMPLETED);
        um.setCompletedAt(LocalDateTime.now());

        experienceService.giveExperience(
                um.getUsers().getId(),
                um.getExperience(),
                SourceType.MISSION_COMPLETE,
                um.getMissions().getId()
        );
    }

    private void checkCountMission(UserMissions um) {
        Missions m = um.getMissions();

        // 아직 기간 안 끝났으면 체크 안 함 (정책에 따라 변경 가능)
        if (LocalDate.now().isBefore(m.getEndDate())) {
            return;
        }

        Long spendingCount = usageHistoryRepository.countUsageHistory(
                um.getUsers(),
                m.getStartDate(),
                m.getEndDate()
        );

        long count = spendingCount != null ? spendingCount : 0L;

        if (count >= m.getMaxInt()) {
            completeMission(um);
        }
    }


    // 출석
    private void checkAttendanceMission(UserMissions um) {
        completeMission(um);
    }
}


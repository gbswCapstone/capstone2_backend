package Capstone.capstoneProject.service.mission;

import Capstone.capstoneProject.entity.mission.Missions;
import Capstone.capstoneProject.entity.mission.UserMissions;
import Capstone.capstoneProject.enums.HistoryType;
import Capstone.capstoneProject.enums.MissionStatusType;
import Capstone.capstoneProject.enums.MissionType;
import Capstone.capstoneProject.enums.SourceType;
import Capstone.capstoneProject.repository.experience.ExperienceHistoryRepository;
import Capstone.capstoneProject.repository.usage.UsageHistoryRepository;
import Capstone.capstoneProject.repository.mission.UserMissionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MissionProgressService {
    private final UserMissionRepository userMissionRepository;
    private final ExperienceHistoryRepository experienceHistoryRepository;
    private final UsageHistoryRepository usageHistoryRepository;
    private final ExperienceService experienceService;

    @Transactional
    public void checkAttendance(Long userId) {
        LocalDate today = LocalDate.now();

        List<UserMissions> missions =
                userMissionRepository.findTodayAttendanceMissions(userId, today);
        for (UserMissions um : missions) {
            // 오늘 완료 안 했으면 스킵
            if (um.getCompletedAt() == null ||
                    !um.getCompletedAt().toLocalDate().isEqual(today)) {
                continue;
            }
            // 이미 보상 받았는지 체크
            boolean rewarded = experienceHistoryRepository
                    .existsByUser_IdAndSourceTypeAndSourceId(
                            userId,
                            SourceType.ATTENDANCE_REWARD,
                            um.getId()
                    );
            if (rewarded) {
                continue;
            }
            // 경험치 지급
            evaluateMission(um);
        }
    }

    // 출석
    private void checkAttendanceMission(UserMissions um) {
        completeCheckAttendanceMission(um);
    }

    public void checkMissionProgress(Long userId) {
        List<UserMissions> activeMissions =
                userMissionRepository.findActiveMissions(userId, LocalDate.now());

        for (UserMissions userMission : activeMissions) {
            evaluateMission(userMission);
        }
    }

    private void evaluateMission(UserMissions userMission) {
        Missions mission = userMission.getMissions();

        switch (mission.getMissionType()) {
            case SPENDING_COUNT -> checkCountMission(userMission);

            case INCOME_GOAL, OUTLAY_GOAL, MONTHLY_OUTLAY_GOAL ->
                    checkAmountMission(userMission);

            case NO_OUTLAY -> checkNoExpenseMission(userMission);

//            case ATTENDANCE_CHECK -> checkAttendanceMission(userMission);
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

    // 출석 미션 저장
    private void completeCheckAttendanceMission(UserMissions um) {
        if (um.getMissionStatusType() == MissionStatusType.COMPLETED) return;

        um.setMissionStatusType(MissionStatusType.COMPLETED);
        um.setCompletedAt(LocalDateTime.now());

        experienceService.giveExperience(
                um.getUsers().getId(),
                um.getExperience(),
                SourceType.ATTENDANCE_REWARD,
                um.getId()
        );
    }


    // 미션 저장
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



}


package Capstone.capstoneProject.service;

import Capstone.capstoneProject.dto.Missions.*;
import Capstone.capstoneProject.dto.MonthGoalMissionDTO;
import Capstone.capstoneProject.entity.Missions.Missions;
import Capstone.capstoneProject.entity.Missions.UserMissions;
import Capstone.capstoneProject.entity.Users.Users;
import Capstone.capstoneProject.enums.*;
import Capstone.capstoneProject.exceptions.badRequest.InvalidMissionTypeException;
import Capstone.capstoneProject.exceptions.conflict.AlreadyCompletedException;
import Capstone.capstoneProject.repository.*;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class MissionService {
    private final AuthenticatedUserUtils authenticatedUserUtils;
    private final MissionRepository missionRepository;
    private final UserMissionRepository userMissionRepository;
    private final UserMissionRepositoryCustom userMissionRepositoryCustom;
    private final UsageHistoryRepository usageHistoryRepository;
    private final ApplicationEventPublisher eventPublisher;

    // 이번달 목표 미션 생성
    public MonthGoalMissionDTO createMonthGoalMission(MonthGoalMissionRequest request) {
        Users user = authenticatedUserUtils.getCurrentUser();
        // 기간 계산
        LocalDate now = LocalDate.now();// 현재 날짜 기준 계산
        LocalDate firstDay = now.withDayOfMonth(1); // 이번 달 1일
        LocalDate lastDay = now.with(TemporalAdjusters.lastDayOfMonth()); // 이번 달 마지막 날

        Missions missions = Missions.builder()
                .missionType(MissionType.MONTHLY_OUTLAY_GOAL)
                .title("이번 달 목표 지출")
                .category(null)
                .maxInt(0)
                .goalAmount(request.getPrice())
                .experience(80)
                .startDate(firstDay)
                .endDate(lastDay)
                .build();
        missionRepository.save(missions);

        UserMissions userMissions = UserMissions.builder()
                .users(user)
                .missions(missions)
                .missionStatusType(MissionStatusType.PROGRESS)
                .completedAt(null)
                .currentStreak(0)
                .experience(missions.getExperience())
                .build();
        userMissionRepository.save(userMissions);
        boolean isSet = true;
        BigDecimal currentPrice = userMissions.getCurrentStreak() > 0
                ? BigDecimal.valueOf(userMissions.getCurrentStreak())
                : BigDecimal.ZERO;
        // 목표까지 남은 금액 계산
        BigDecimal remainingAmount = missions.getGoalAmount() != null
                ? missions.getGoalAmount().subtract(currentPrice)
                : BigDecimal.ZERO;

        return MonthGoalMissionDTO.from(isSet, missions, userMissions, currentPrice, remainingAmount);
    }


    // 이번달 목표 미션 조회
    public MonthGoalMissionDTO getMonthGoalMission() {
        Users user = authenticatedUserUtils.getCurrentUser();

        Optional<UserMissions> optionalUserMission = userMissionRepository
                .findByUsersAndMissions_MissionType(user, MissionType.MONTHLY_OUTLAY_GOAL);

        boolean isSet = optionalUserMission.isPresent();

        Missions mission = null;
        UserMissions userMission = null;
        BigDecimal currentPrice = BigDecimal.ZERO;
        BigDecimal remainingAmount = BigDecimal.ZERO;

        if (isSet) {
            userMission = optionalUserMission.get();
            mission = userMission.getMissions();

            // 현재 지출 계산 (예시)
            currentPrice = userMission.getCurrentStreak() > 0
                    ? BigDecimal.valueOf(userMission.getCurrentStreak())
                    : BigDecimal.ZERO;

            // 목표까지 남은 금액 계산
            remainingAmount = mission.getGoalAmount() != null
                    ? mission.getGoalAmount().subtract(currentPrice)
                    : BigDecimal.ZERO;
            if (remainingAmount.compareTo(BigDecimal.ZERO) < 0) {
                remainingAmount = BigDecimal.ZERO;
            }
        }
        // DTO 반환
        return MonthGoalMissionDTO.builder()
                .id(mission != null ? mission.getId() : null)
                .missionType(mission != null ? mission.getMissionType() : null)
                .title(mission != null ? mission.getTitle() : null)
                .status(userMission != null ? userMission.getMissionStatusType() : null)
                .startDate(mission != null ? mission.getStartDate() : null)
                .endDate(mission != null ? mission.getEndDate() : null)
                .createdAt(mission != null ? mission.getCreatedAt() : null)
                .currentPrice(currentPrice)
                .remainingAmount(remainingAmount)
                .goalAmount(mission != null ? mission.getGoalAmount() : null)
                .build();
    }

    // 개인 미션 생성
    public MissionResponse createPersonalMission(MissionCreate request) {
        Users user = authenticatedUserUtils.getCurrentUser();
        Missions missions;
        if (request.getMissionType() == MissionType.SPENDING_COUNT) {
            missions = Missions.builder()
                    .challenges(null)
                    .missionType(MissionType.SPENDING_COUNT)
                    .title(request.getTitle())
                    .category(request.getCategory())
                    .maxInt(request.getMexInt())
                    .goalAmount(null)
                    .experience(30) // 기본 경험치
                    .startDate(request.getStartDate())
                    .endDate(request.getEndDate())
                    .build();
        } else if (request.getMissionType() != MissionType.NO_OUTLAY) {
            missions = Missions.builder()
                    .challenges(null)
                    .missionType(MissionType.NO_OUTLAY)
                    .title(request.getTitle())
                    .category(request.getCategory())
                    .maxInt(0)
                    .goalAmount(null)
                    .experience(50)
                    .startDate(request.getStartDate())
                    .endDate(request.getEndDate())
                    .build();
        } else if (request.getMissionType() != MissionType.INCOME_GOAL || request.getMissionType() != MissionType.OUTLAY_GOAL) {
            missions = Missions.builder()
                    .challenges(null)
                    .missionType(request.getMissionType())
                    .title(request.getTitle())
                    .category(request.getCategory())
                    .maxInt(0)
                    .goalAmount(request.getGoalAmount())
                    .experience(30)
                    .startDate(request.getStartDate())
                    .endDate(request.getEndDate())
                    .build();
        } else {
            throw new InvalidMissionTypeException("사용할 수 없는 미션 타입 입니다.");
        }
        missionRepository.save(missions);
        UserMissions userMissions = UserMissions.builder()
                .users(user)
                .missions(missions)
                .missionStatusType(MissionStatusType.PROGRESS)
                .completedAt(null)
                .currentStreak(0)
                .experience(missions.getExperience())
                .build();
        userMissionRepository.save(userMissions);
        return MissionResponse.from(missions, userMissions);
    }

    // 오늘의 출석체크 미션 조회
    public AttendanceMissionDTO getTodayAttendanceMission() {
        Users user = authenticatedUserUtils.getCurrentUser();
        LocalDate today = LocalDate.now();

        // 오늘 날짜 출석체크 미션 조회
        UserMissions mission = userMissionRepository
                .findByUsersAndMissions_MissionTypeAndMissions_StartDate(user, MissionType.ATTENDANCE_CHECK, today)
                .orElse(null); // 오늘 미션이 없는 경우 null 처리

        if (mission == null) {
            return null;
        }

        // DTO로 변환
        return AttendanceMissionDTO.builder()
                .id(mission.getMissions().getId())
                .status(mission.getMissionStatusType())
                .title("우거우거에 접속하기")
                .startDate(mission.getMissions().getStartDate())
                .endDate(mission.getMissions().getEndDate())
                .build();
    }

    // 출석미션 완료
    public void checkDailyAttendance() {
        Users user = authenticatedUserUtils.getCurrentUser();
        LocalDate today = LocalDate.now();
        UserMissions mission = userMissionRepository
                .findByUsersAndMissions_MissionTypeAndMissions_StartDate(user, MissionType.ATTENDANCE_CHECK, today)
                .orElseThrow();

        if (mission.getMissionStatusType() == MissionStatusType.COMPLETED) {
            return; // 이미 완료, 아무것도 안 함
        }

        mission.setMissionStatusType(MissionStatusType.COMPLETED);
        mission.setCompletedAt(LocalDateTime.now());
        mission.setCurrentStreak(mission.getCurrentStreak() + 1);
        userMissionRepository.save(mission);

        // 출석체크 이벤트 발행
        eventPublisher.publishEvent(
                new AttendanceEvent(user.getId())
        );
    }

    // 유저 전체 미션 조회
    public List<MissionListDTO> getMissions(DateSortType dateSortType, MissionCategory category) {
        Users user = authenticatedUserUtils.getCurrentUser();
        List<UserMissions>  userMissions = userMissionRepositoryCustom.findAllByUsersWithMissions(user, category, dateSortType);


        return userMissions.stream()
                .map(um -> {
                    BigDecimal currentAmount = getCalculatedAmount(user, um);
                    return MissionListDTO.from(um.getMissions(), um, currentAmount);
                })
                .collect(Collectors.toList());
    }

    private BigDecimal getCalculatedAmount(Users user, UserMissions um) {
        Missions m = um.getMissions();

        return switch (m.getMissionType()) {
            case SPENDING_COUNT -> {
                Long count = usageHistoryRepository.countUsageHistory(user, m.getStartDate(), m.getEndDate());
                yield BigDecimal.valueOf(count != null ? count : 0L);
            }

            case OUTLAY_GOAL, INCOME_GOAL, MONTHLY_OUTLAY_GOAL ->
                    usageHistoryRepository.calculateTotalSpending(user, m.getCategory(), m.getStartDate(), m.getEndDate());

            case NO_OUTLAY -> {
                // (오늘까지 진행된 날수) - (지출이 있었던 날수)
                long totalDays = ChronoUnit.DAYS.between(m.getStartDate(), LocalDate.now()) + 1;
                Long spendingDays = usageHistoryRepository.countSpendingDays(user, m.getStartDate(), m.getEndDate());
                yield BigDecimal.valueOf(Math.max(0, totalDays - (spendingDays != null ? spendingDays : 0)));
            }

            case ATTENDANCE_CHECK -> BigDecimal.valueOf(um.getCurrentStreak());

            default -> BigDecimal.ZERO;
        };
    }


}

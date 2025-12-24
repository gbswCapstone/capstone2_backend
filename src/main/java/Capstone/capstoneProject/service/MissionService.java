package Capstone.capstoneProject.service;

import Capstone.capstoneProject.dto.Missions.MissionListDTO;
import Capstone.capstoneProject.dto.Missions.MissionResponse;
import Capstone.capstoneProject.dto.Missions.MissionCreate;
import Capstone.capstoneProject.entity.Missions.Missions;
import Capstone.capstoneProject.entity.Missions.UserMissions;
import Capstone.capstoneProject.entity.Users.Users;
import Capstone.capstoneProject.enums.*;
import Capstone.capstoneProject.exceptions.badRequest.InvalidMissionTypeException;
import Capstone.capstoneProject.repository.*;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
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

    public MissionResponse createMonthGoalMission(BigDecimal price) {
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
                .goalAmount(price)
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

        return MissionResponse.from(missions, userMissions);

    }

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



//    public List<MissionListDTO> getMissions(SortType sortType) {
//        Users user = authenticatedUserUtils.getCurrentUser();
//        List<UserMissions> userMissions = userMissionRepository.findAllByUsers(user);
//        List<Missions> missions = missionRepository.findAllById(userMissions.get().getMissions().getId());
//
//
//        // 현재 진행률
//        BigDecimal currentAmount;
//        return MissionListDTO.from(missions, userMissions, currentAmount);
//    }

}

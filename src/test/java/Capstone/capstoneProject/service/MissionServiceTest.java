package Capstone.capstoneProject.service;

import Capstone.capstoneProject.dto.Missions.MissionCreate;
import Capstone.capstoneProject.dto.Missions.MissionResponse;
import Capstone.capstoneProject.entity.Missions.Missions;
import Capstone.capstoneProject.entity.Missions.UserMissions;
import Capstone.capstoneProject.entity.Users.Users;
import Capstone.capstoneProject.enums.MissionStatusType;
import Capstone.capstoneProject.enums.MissionType;
import Capstone.capstoneProject.enums.UsageCategory;
import Capstone.capstoneProject.exceptions.badRequest.InvalidMissionTypeException;
import Capstone.capstoneProject.repository.MissionRepository;
import Capstone.capstoneProject.repository.UserMissionRepository;
import Capstone.capstoneProject.repository.UserMissionRepositoryCustom;
import Capstone.capstoneProject.repository.UsageHistoryRepository;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MissionServiceTest {

    @Mock private AuthenticatedUserUtils authenticatedUserUtils;
    @Mock private MissionRepository missionRepository;
    @Mock private UserMissionRepository userMissionRepository;
    @Mock private UserMissionRepositoryCustom userMissionRepositoryCustom;
    @Mock private UsageHistoryRepository usageHistoryRepository;
    @Mock private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private MissionService missionService;

    private final Users testUser = Users.builder()
            .id(1L).email("test@test.com").password("pw").provider("local").providerId("test").build();

    @Test
    @DisplayName("SPENDING_COUNT 미션 생성 - 올바른 타입으로 저장됨")
    void createSpendingCountMission() {
        when(authenticatedUserUtils.getCurrentUser()).thenReturn(testUser);

        Missions saved = Missions.builder()
                .missionType(MissionType.SPENDING_COUNT).title("지출 횟수 미션")
                .maxInt(5).experience(30)
                .startDate(LocalDate.now()).endDate(LocalDate.now().plusDays(7))
                .build();
        when(missionRepository.save(any())).thenReturn(saved);

        UserMissions savedUM = UserMissions.builder()
                .users(testUser).missions(saved)
                .missionStatusType(MissionStatusType.PROGRESS).currentStreak(0).experience(30)
                .build();
        when(userMissionRepository.save(any())).thenReturn(savedUM);

        MissionCreate request = MissionCreate.builder()
                .title("지출 횟수 미션").missionType(MissionType.SPENDING_COUNT)
                .mexInt(5).startDate(LocalDate.now()).endDate(LocalDate.now().plusDays(7))
                .build();

        missionService.createPersonalMission(request);

        ArgumentCaptor<Missions> captor = ArgumentCaptor.forClass(Missions.class);
        verify(missionRepository).save(captor.capture());
        assertThat(captor.getValue().getMissionType()).isEqualTo(MissionType.SPENDING_COUNT);
        assertThat(captor.getValue().getMaxInt()).isEqualTo(5);
    }

    @Test
    @DisplayName("NO_OUTLAY 미션 생성 - 올바른 타입으로 저장됨 (기존 != 버그 수정 검증)")
    void createNoOutlayMission() {
        when(authenticatedUserUtils.getCurrentUser()).thenReturn(testUser);

        Missions saved = Missions.builder()
                .missionType(MissionType.NO_OUTLAY).title("무지출 미션")
                .maxInt(0).experience(50)
                .startDate(LocalDate.now()).endDate(LocalDate.now().plusDays(7))
                .build();
        when(missionRepository.save(any())).thenReturn(saved);

        UserMissions savedUM = UserMissions.builder()
                .users(testUser).missions(saved)
                .missionStatusType(MissionStatusType.PROGRESS).currentStreak(0).experience(50)
                .build();
        when(userMissionRepository.save(any())).thenReturn(savedUM);

        MissionCreate request = MissionCreate.builder()
                .title("무지출 미션").missionType(MissionType.NO_OUTLAY)
                .startDate(LocalDate.now()).endDate(LocalDate.now().plusDays(7))
                .build();

        missionService.createPersonalMission(request);

        ArgumentCaptor<Missions> captor = ArgumentCaptor.forClass(Missions.class);
        verify(missionRepository).save(captor.capture());
        assertThat(captor.getValue().getMissionType()).isEqualTo(MissionType.NO_OUTLAY);
        assertThat(captor.getValue().getExperience()).isEqualTo(50);
    }

    @Test
    @DisplayName("OUTLAY_GOAL 미션 생성 - 목표 금액이 올바르게 저장됨")
    void createOutlayGoalMission() {
        when(authenticatedUserUtils.getCurrentUser()).thenReturn(testUser);

        BigDecimal goalAmount = new BigDecimal("100000");
        Missions saved = Missions.builder()
                .missionType(MissionType.OUTLAY_GOAL).title("지출 목표 미션")
                .goalAmount(goalAmount).experience(30)
                .startDate(LocalDate.now()).endDate(LocalDate.now().plusMonths(1))
                .build();
        when(missionRepository.save(any())).thenReturn(saved);

        UserMissions savedUM = UserMissions.builder()
                .users(testUser).missions(saved)
                .missionStatusType(MissionStatusType.PROGRESS).currentStreak(0).experience(30)
                .build();
        when(userMissionRepository.save(any())).thenReturn(savedUM);

        MissionCreate request = MissionCreate.builder()
                .title("지출 목표 미션").missionType(MissionType.OUTLAY_GOAL)
                .goalAmount(goalAmount)
                .startDate(LocalDate.now()).endDate(LocalDate.now().plusMonths(1))
                .build();

        missionService.createPersonalMission(request);

        ArgumentCaptor<Missions> captor = ArgumentCaptor.forClass(Missions.class);
        verify(missionRepository).save(captor.capture());
        assertThat(captor.getValue().getMissionType()).isEqualTo(MissionType.OUTLAY_GOAL);
        assertThat(captor.getValue().getGoalAmount()).isEqualByComparingTo(goalAmount);
    }

    @Test
    @DisplayName("INCOME_GOAL 미션 생성 - 목표 금액이 올바르게 저장됨")
    void createIncomeGoalMission() {
        when(authenticatedUserUtils.getCurrentUser()).thenReturn(testUser);

        BigDecimal goalAmount = new BigDecimal("500000");
        Missions saved = Missions.builder()
                .missionType(MissionType.INCOME_GOAL).title("수입 목표 미션")
                .goalAmount(goalAmount).experience(30)
                .startDate(LocalDate.now()).endDate(LocalDate.now().plusMonths(1))
                .build();
        when(missionRepository.save(any())).thenReturn(saved);

        UserMissions savedUM = UserMissions.builder()
                .users(testUser).missions(saved)
                .missionStatusType(MissionStatusType.PROGRESS).currentStreak(0).experience(30)
                .build();
        when(userMissionRepository.save(any())).thenReturn(savedUM);

        MissionCreate request = MissionCreate.builder()
                .title("수입 목표 미션").missionType(MissionType.INCOME_GOAL)
                .goalAmount(goalAmount)
                .startDate(LocalDate.now()).endDate(LocalDate.now().plusMonths(1))
                .build();

        missionService.createPersonalMission(request);

        ArgumentCaptor<Missions> captor = ArgumentCaptor.forClass(Missions.class);
        verify(missionRepository).save(captor.capture());
        assertThat(captor.getValue().getMissionType()).isEqualTo(MissionType.INCOME_GOAL);
    }

    @Test
    @DisplayName("ATTENDANCE_CHECK 타입은 개인 미션 생성 불가 - InvalidMissionTypeException 발생")
    void attendanceCheckTypeThrowsException() {
        when(authenticatedUserUtils.getCurrentUser()).thenReturn(testUser);

        MissionCreate request = MissionCreate.builder()
                .title("출석 미션").missionType(MissionType.ATTENDANCE_CHECK)
                .startDate(LocalDate.now()).endDate(LocalDate.now().plusDays(1))
                .build();

        assertThatThrownBy(() -> missionService.createPersonalMission(request))
                .isInstanceOf(InvalidMissionTypeException.class);
    }
}

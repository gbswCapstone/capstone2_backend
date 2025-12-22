package Capstone.capstoneProject.service;

import Capstone.capstoneProject.dto.Missions.MissionListDTO;
import Capstone.capstoneProject.dto.Missions.MissionResponse;
import Capstone.capstoneProject.dto.Missions.MissionCreate;
import Capstone.capstoneProject.entity.Chats.ChatRooms;
import Capstone.capstoneProject.entity.Missions.MissionLevels;
import Capstone.capstoneProject.entity.Missions.Missions;
import Capstone.capstoneProject.entity.Missions.UserMissions;
import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.enums.MissionStatusType;
import Capstone.capstoneProject.enums.MissionType;
import Capstone.capstoneProject.enums.PeriodType;
import Capstone.capstoneProject.enums.SortType;
import Capstone.capstoneProject.exceptions.conflict.AlreadyJoinedException;
import Capstone.capstoneProject.exceptions.forbidden.ChallengeAccessDeniedException;
import Capstone.capstoneProject.exceptions.forbidden.ChatRoomAccessDeniedException;
import Capstone.capstoneProject.exceptions.notfound.ChatRoomNotFoundException;
import Capstone.capstoneProject.exceptions.notfound.MissionNotFoundException;
import Capstone.capstoneProject.repository.*;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class MissionService {
    private final AuthenticatedUserUtils authenticatedUserUtils;
    private final MissionRepository missionRepository;
    private final UserMissionRepository userMissionRepository;
    private final MissionLevelRepository missionLevelRepository;
    private final ChatRoomUsersRepository chatRoomUsersRepository;
    private final ChatRoomsRepository chatRoomsRepository;
    private final ChallengeMessageService challengeMessageService;
    private final ChallengeUsersRepository challengeUsersRepository;

    public MissionResponse createPersonalMission(MissionCreate request) {
        Users user = authenticatedUserUtils.getCurrentUser();

        Missions missions = Missions.builder()
                .challenges(null)
                .missionType(MissionType.CUSTOM)
                .title(request.getTitle())
                .rule(request.getRule())
                .goalAmount(request.getGoalAmount())
                .experience(20)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();
        missionRepository.save(missions);

        MissionLevels missionLevels = MissionLevels.builder()
                .missions(missions)
                .periodType(PeriodType.DAY)
                .experience(missions.getExperience())
                .build();
        missionLevelRepository.save(missionLevels);

        UserMissions userMissions = UserMissions.builder()
                .users(user)
                .missions(missions)
                .missionStatusType(MissionStatusType.PROGRESS)
                .completedAt(null)
                .experience(missions.getExperience())
                .build();
        userMissionRepository.save(userMissions);

        return MissionResponse.from(missions, userMissions, missionLevels);
    }

    public MissionResponse createChallengeMission(String roomId, MissionCreate request) {
        Users user = authenticatedUserUtils.getCurrentUser();

        ChatRooms chatRooms = chatRoomsRepository.findByRoomId(roomId)
                        .orElseThrow(() -> new ChatRoomNotFoundException("해당 채팅방을 찾을 수 없습니다."));

        chatRoomUsersRepository.findByChatRooms_RoomIdAndUsers(roomId, user)
                .orElseThrow(() -> new ChatRoomAccessDeniedException("해당 채팅방 유저가 아닙니다."));

        Missions missions = Missions.builder()
                .challenges(chatRooms.getChallenge())
                .missionType(MissionType.CHALLENGE)
                .title(request.getTitle())
                .rule(request.getRule())
                .goalAmount(request.getGoalAmount())
                .experience(30) // 챌린지 미션
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();
        missionRepository.save(missions);

        MissionLevels missionLevels = MissionLevels.builder()
                .missions(missions)
                .periodType(PeriodType.DAY)
                .experience(missions.getExperience())
                .build();
        missionLevelRepository.save(missionLevels);

        UserMissions userMissions = UserMissions.builder()
                .users(user)
                .missions(missions)
                .missionStatusType(MissionStatusType.PROGRESS)
                .completedAt(null)
                .experience(missions.getExperience())
                .build();
        userMissionRepository.save(userMissions);

        // 채팅방에 미션 메시지 전송
        challengeMessageService.saveAndSendMissionMessage(roomId, missions, user);

        return MissionResponse.from(missions, userMissions, missionLevels);
    }

    public void joinChallengeMission(Long missionId) {
        Users user = authenticatedUserUtils.getCurrentUser();
        Missions missions = missionRepository.findById(missionId)
                .orElseThrow(() -> new MissionNotFoundException("해당 미션을 찾을 수 없습니다."));
        boolean joined =  challengeUsersRepository.existsByChallengeIdAndUserId(missions.getChallenges().getId(), user.getId());
        if (!joined) {
            throw new ChallengeAccessDeniedException("챌린지방 참가자가 아닙니다.");
        }

        if (userMissionRepository.existsByUsersAndMissions(user, missions)) {
            throw new AlreadyJoinedException("이미 참여한 미션입니다.");
        }

        UserMissions userMissions = UserMissions.builder()
                .users(user)
                .missions(missions)
                .missionStatusType(MissionStatusType.PROGRESS)
                .completedAt(null)
                .experience(missions.getExperience())
                .build();
        userMissionRepository.save(userMissions);
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

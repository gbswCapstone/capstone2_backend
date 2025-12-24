package Capstone.capstoneProject.service;

import Capstone.capstoneProject.dto.Chats.ChatMessageDTO;
import Capstone.capstoneProject.dto.Missions.MissionCreate;
import Capstone.capstoneProject.dto.Missions.MissionResponse;
import Capstone.capstoneProject.entity.Chats.ChatMessages;
import Capstone.capstoneProject.entity.Chats.ChatRooms;
import Capstone.capstoneProject.entity.Missions.Missions;
import Capstone.capstoneProject.entity.Missions.UserMissions;
import Capstone.capstoneProject.entity.Users.Users;
import Capstone.capstoneProject.enums.MissionStatusType;
import Capstone.capstoneProject.enums.MissionType;
import Capstone.capstoneProject.enums.PeriodType;
import Capstone.capstoneProject.exceptions.badRequest.InvalidMissionTypeException;
import Capstone.capstoneProject.exceptions.conflict.AlreadyJoinedException;
import Capstone.capstoneProject.exceptions.forbidden.ChallengeAccessDeniedException;
import Capstone.capstoneProject.exceptions.forbidden.ChatRoomAccessDeniedException;
import Capstone.capstoneProject.exceptions.notfound.ChatRoomMessageNotFoundException;
import Capstone.capstoneProject.exceptions.notfound.ChatRoomNotFoundException;
import Capstone.capstoneProject.exceptions.notfound.MissionNotFoundException;
import Capstone.capstoneProject.repository.*;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChallengeMissionService {

    private final AuthenticatedUserUtils authenticatedUserUtils;
    private final ChatRoomsRepository chatRoomsRepository;
    private final ChatRoomUsersRepository chatRoomUsersRepository;
    private final MissionRepository missionRepository;
    private final UserMissionRepository userMissionRepository;
    private final ChallengeMessageService challengeMessageService;
    private final ChatMessagesRepository chatMessagesRepository;
    private final ChallengeUsersRepository challengeUsersRepository;


    public MissionResponse createChallengeMission(String roomId, MissionCreate request) {
        Users user = authenticatedUserUtils.getCurrentUser();

        ChatRooms chatRooms = chatRoomsRepository.findByRoomId(roomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("해당 채팅방을 찾을 수 없습니다."));

        chatRoomUsersRepository.findByChatRooms_RoomIdAndUsers(roomId, user)
                .orElseThrow(() -> new ChatRoomAccessDeniedException("해당 채팅방 유저가 아닙니다."));

        Missions missions;
        if (request.getMissionType() == MissionType.SPENDING_COUNT) {
            missions = Missions.builder()
                    .challenges(chatRooms.getChallenge())
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
                    .challenges(chatRooms.getChallenge())
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
                    .challenges(chatRooms.getChallenge())
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

        // 채팅방에 미션 메시지 전송
        challengeMessageService.saveAndSendMissionMessage(roomId, missions, user);

        return MissionResponse.from(missions, userMissions);
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
                .currentStreak(0)
                .experience(missions.getExperience())
                .build();
        userMissionRepository.save(userMissions);
        // 새로운 인원수 계산
        int newCount = userMissionRepository.countByMissionsId(missionId);
        ChatMessages chatMessages = chatMessagesRepository.findByMissionsAndIsDeletedFalse(missions)
                .orElseThrow(() -> new ChatRoomMessageNotFoundException("해당 메시지를 찾을 수 없습니다."));

        // 채팅방에 바로 반영
        ChatMessageDTO dto = ChatMessageDTO.missionShare(chatMessages, missions, newCount);

        // 수정한거 채팅방에 바로 반영
        challengeMessageService.updateMissionMessage(chatMessages.getChatRooms().getRoomId(), dto);
    }
}

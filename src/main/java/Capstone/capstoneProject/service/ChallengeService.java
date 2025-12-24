package Capstone.capstoneProject.service;

import Capstone.capstoneProject.dto.Challenges.ChallengeCreate;
import Capstone.capstoneProject.dto.Challenges.ChallengeDetailResponse;
import Capstone.capstoneProject.dto.Challenges.ChallengeJoinResponse;
import Capstone.capstoneProject.dto.Challenges.ChallengeListDTO;
import Capstone.capstoneProject.dto.LikeResponseDTO;
import Capstone.capstoneProject.entity.Chats.ChatRoomUsers;
import Capstone.capstoneProject.entity.Chats.ChatRooms;
import Capstone.capstoneProject.entity.Users.Users;
import Capstone.capstoneProject.entity.challenges.*;
import Capstone.capstoneProject.enums.ChatRoomRole;
import Capstone.capstoneProject.enums.SortType;
import Capstone.capstoneProject.enums.UserJobs;
import Capstone.capstoneProject.exceptions.conflict.AlreadyJoinedException;
import Capstone.capstoneProject.exceptions.conflict.ChallengeFullException;
import Capstone.capstoneProject.exceptions.forbidden.ChallengeAccessDeniedException;
import Capstone.capstoneProject.exceptions.forbidden.ChatRoomAccessDeniedException;
import Capstone.capstoneProject.exceptions.forbidden.NotChallengeOwnerException;
import Capstone.capstoneProject.exceptions.notfound.ChallengeNotFoundException;
import Capstone.capstoneProject.exceptions.notfound.ChatRoomNotFoundException;
import Capstone.capstoneProject.repository.*;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final AuthenticatedUserUtils authenticatedUserUtils;
    private final ChallengeHashtagRepository challengeHashtagRepository;
    private final HashtagRepository hashtagRepository;
    private final ChallengeUsersRepository challengeUsersRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final ChallengeChatRoomService challengeChatRoomService;
    private final ChatRoomUsersRepository chatRoomUsersRepository;
    private final ChatRoomsRepository chatRoomsRepository;
    private final ChallengeMessageService challengeMessageService;


    public ChallengeDetailResponse create(ChallengeCreate dto) {
        // user 정보 가져오기 (baarer token에서 추출)
        Users user = authenticatedUserUtils.getCurrentUser();

        Challenges challenge = Challenges.builder()
                .createdBy(user)
                .title(dto.getTitle())
                .maxPersonnel(dto.getMaxPersonnel())
                .job(dto.getJob())
                .goal(dto.getGoal())
                .image(dto.getImage())
                .build();
        challengeRepository.save(challenge);

        // 챗방생성
        ChatRooms chatRooms = challengeChatRoomService.createRoom(challenge);

        // 작성자를 자동 참여자로 추가
        ChallengeUsers challengeUser = ChallengeUsers.builder()
                .challenge(challenge)
                .user(user)
                .joinedAt(LocalDateTime.now())
                .build();
        challengeUsersRepository.save(challengeUser);

        // 채팅방에도 작성자를 자동 참여자로 추가
        ChatRoomUsers chatRoomUser = ChatRoomUsers.builder()
                .chatRooms(chatRooms)
                .users(user)
                .createdAt(LocalDateTime.now())
                .chatRoomRole(ChatRoomRole.HOST) // 작성자를 방장으로 추가
                .build();
        chatRoomUsersRepository.save(chatRoomUser);

        // 해시태그 넣어주기
        if (dto.getHashtags() != null && !dto.getHashtags().isEmpty()) {
            List<Hashtag> hashtags = new ArrayList<>();

            for (String name : dto.getHashtags()) {
                Hashtag tag = hashtagRepository.findByName(name)
                        .orElseGet(() -> {
                            Hashtag newTag = new Hashtag();
                            newTag.setName(name);
                            return hashtagRepository.save(newTag);
                        });
                hashtags.add(tag);
            }

            List<ChallengeHashtag> challengeHashtags = hashtags.stream()
                    .map(tag -> ChallengeHashtag.of(challenge, tag))
                    .toList();
            challengeHashtagRepository.saveAll(challengeHashtags);
        }

        boolean isLiked = false;
        ChallengeDetailResponse challengeDetailResponse = ChallengeDetailResponse.fromEntity(challenge, isLiked);

        // 작성자이기 때문에 참여
        challengeDetailResponse.setJoined(true);

        chatRoomsRepository.findByChallenge(challenge)
                .ifPresent(cr -> challengeDetailResponse.setRoomId(cr.getRoomId()));
        return challengeDetailResponse;
    }


    public List<ChallengeListDTO> getChallengeList(SortType sortType, UserJobs job) {
        Users user = authenticatedUserUtils.getCurrentUser();
        SortType finalSort = (sortType == null) ? SortType.RECENT : sortType;
        UserJobs finalJob = (job == null) ? UserJobs.NONE : job;

        List<Challenges> challenges;

        if (finalSort == SortType.POPULAR) {
            challenges = (finalJob == UserJobs.NONE)
                    ? challengeRepository.findAllByDeletedAtIsNullOrderByLikeCountDescCreatedAtDesc()
                    : challengeRepository.findAllByJobAndDeletedAtIsNullOrderByLikeCountDescCreatedAtDesc(finalJob);
        } else if (finalSort == SortType.OLDEST) {
            challenges = (finalJob == UserJobs.NONE)
                    ? challengeRepository.findAllByDeletedAtIsNullOrderByCreatedAtAsc()
                    : challengeRepository.findAllByJobAndDeletedAtIsNullOrderByCreatedAtAsc(finalJob);
        } else { // RECENT
            challenges = (finalJob == UserJobs.NONE)
                    ? challengeRepository.findAllByDeletedAtIsNullOrderByCreatedAtDesc()
                    : challengeRepository.findAllByJobAndDeletedAtIsNullOrderByCreatedAtDesc(finalJob);
        }

        return challenges.stream()
                .map(ch -> {
                    ChallengeListDTO dto = new ChallengeListDTO(ch);

                    // 참여여부 체크
                    boolean isJoined = challengeUsersRepository.existsByChallengeIdAndUserId(ch.getId(), user.getId());
                    dto.setJoined(isJoined);

                    // 참여한 경우에만 roomId 세팅
                    if (isJoined) {
                        chatRoomsRepository.findByChallenge(ch)
                                .ifPresent(cr -> dto.setRoomId(cr.getRoomId()));
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public LikeResponseDTO toggleLike(Long id) {
        // user 정보 가져오기 (baarer token에서 추출)
        Users user = authenticatedUserUtils.getCurrentUser();
        Challenges challenges = challengeRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ChallengeNotFoundException("해당 챌린지 방을 찾을 수 없습니다."));

        // 좋아요 여부 판별
        Optional<ChallengeLikes> existing = likeRepository.findByUserAndChallenges(user, challenges);
        boolean liked;
        if (existing.isPresent()) {
            likeRepository.delete(existing.get()); // 좋아요 취소

            challenges.setLikeCount(challenges.getLikeCount() - 1);
            challengeRepository.save(challenges);
            liked = false;
        } else {
            ChallengeLikes like = ChallengeLikes.builder().user(user).challenges(challenges).build();
            likeRepository.save(like); // 좋아요 추가

            challenges.setLikeCount(challenges.getLikeCount() + 1);
            challengeRepository.save(challenges);
            liked = true;
        }
        int likeCount = likeRepository.countByChallenges(challenges);
        return new LikeResponseDTO(liked, likeCount);
    }

    public Challenges findById(Long id) {
        Challenges challenges = challengeRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ChallengeNotFoundException("해당 챌린지방을 찾을 수 없습니다."));
        return challenges;
    }

    public ChallengeDetailResponse getChallenge(Long id) {
        Challenges challenges = findById(id);
        // user 정보 가져오기 (baarer token에서 추출)
        Users user = authenticatedUserUtils.getCurrentUser();

        boolean isLiked = isLikedByUser(challenges, user);


        ChallengeDetailResponse challengeDetailResponse = ChallengeDetailResponse.fromEntity(challenges, isLiked);

        // 작성자이기 때문에 참여
        challengeDetailResponse.setJoined(true);

        chatRoomsRepository.findByChallenge(challenges)
                .ifPresent(cr -> challengeDetailResponse.setRoomId(cr.getRoomId()));
        return challengeDetailResponse;
    }

    public boolean isLikedByUser(Challenges challenges, Users user) {
        boolean isLiked = likeRepository.findByUserAndChallenges(userRepository.findById(user.getId())
                .orElseThrow(), challenges).isPresent();
        return isLiked;
    }

    @Transactional
    public ChallengeDetailResponse update(Long id, ChallengeCreate request) {
        Challenges challenges = challengeRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ChallengeNotFoundException("해당 챌린지 방을 찾을 수 없습니다."));
        // user 정보 가져오기 (bearer token에서 추출)
        Users user = authenticatedUserUtils.getCurrentUser();

        // 수정하는 사람이 작성자인지 확인
        if (!challenges.getCreatedBy().getId().equals(user.getId())) {
            throw new NotChallengeOwnerException("챌린지 방에 관한 권한이 없습니다.");
        }

        challenges.setTitle(request.getTitle());
        challenges.setImage(request.getImage());
        challenges.setGoal(request.getGoal());
        challenges.setJob(request.getJob());
        challenges.setMaxPersonnel(request.getMaxPersonnel());

        // 해시태그 수정
        if (request.getHashtags() != null) {
            // 기존 연결된 해시태그 가져오기
            List<ChallengeHashtag> existingRelations =
                    challengeHashtagRepository.findByChallenge(challenges);

            // 기존 해시태그 이름 목록
            Set<String> existingNames = existingRelations.stream()
                    .map(ch -> ch.getHashtag().getName())
                    .collect(Collectors.toSet());

            // 새로 들어온 해시태그 이름 목록
            Set<String> newNames = new HashSet<>(request.getHashtags());

            // 삭제해야 할 해시태그 관계 (기존에 있었지만 새 목록에 없는 것)
            List<ChallengeHashtag> relationsToDelete = existingRelations.stream()
                    .filter(ch -> !newNames.contains(ch.getHashtag().getName()))
                    .toList();

            if (!relationsToDelete.isEmpty()) {
                challengeHashtagRepository.deleteAll(relationsToDelete);
            }

            // 추가해야 할 해시태그 이름 목록 (새 목록에는 있지만 기존에 없었던 것)
            List<String> hashtagNamesToAdd = newNames.stream()
                    .filter(name -> !existingNames.contains(name))
                    .toList();

            List<Hashtag> hashtagsToAssociate = hashtagNamesToAdd.stream()
                    .map(name -> hashtagRepository.findByName(name)
                            .orElseGet(() -> {
                                Hashtag newTag = new Hashtag();
                                newTag.setName(name);
                                // 신규 해시태그를 먼저 db에 저장
                                return hashtagRepository.save(newTag);
                            })
                    )
                    .toList();

            List<ChallengeHashtag> newRelations = hashtagsToAssociate.stream()
                    .map(tag -> ChallengeHashtag.of(challenges, tag))
                    .toList();

            // 새로운 해시태그 저장
            if (!newRelations.isEmpty()) {
                challengeHashtagRepository.saveAll(newRelations);
            }
        }
        boolean isLiked = isLikedByUser(challenges, user);
        ChallengeDetailResponse challengeDetailResponse = ChallengeDetailResponse.fromEntity(challenges, isLiked);

        // 작성자이기 때문에 참여
        challengeDetailResponse.setJoined(true);

        chatRoomsRepository.findByChallenge(challenges)
                .ifPresent(cr -> challengeDetailResponse.setRoomId(cr.getRoomId()));
        return challengeDetailResponse;
    }

    public void delete(Long id) {
        Challenges challenges = challengeRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ChallengeNotFoundException("해당 챌린지방을 찾을 수 없습니다."));
        // user 정보 가져오기 (bearer token에서 추출)
        Users user = authenticatedUserUtils.getCurrentUser();
        // 작성자인지 확인
        if (!challenges.getCreatedBy().getId().equals(user.getId())) {
            throw new NotChallengeOwnerException("챌린지 방에 관한 권한이 없습니다.");
        }
        challenges.setDeletedAt(LocalDateTime.now());
        challengeRepository.save(challenges);
    }

    public List<ChallengeListDTO> searchChallenge(String hashtag, String keyword, SortType sortType, UserJobs userJobs) {
        Users user = authenticatedUserUtils.getCurrentUser();
        List<Challenges> challenges = challengeRepository.searchDynamic(hashtag, keyword, sortType, userJobs);
        return challenges.stream()
                .map(ch -> {
                    ChallengeListDTO dto = new ChallengeListDTO(ch);


                    // 참여여부 체크
                    boolean isJoined = ch.getChallengeUsers().stream()
                            .anyMatch(cu -> cu.getUser().getId().equals(user.getId()));
                    dto.setJoined(isJoined);

                    // 참여한 경우에만 roomId 세팅
                    if (isJoined) {
                        chatRoomsRepository.findByChallenge(ch)
                                .ifPresent(cr -> dto.setRoomId(cr.getRoomId()));
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public ChallengeJoinResponse joinChallenge(Long id) {
        Challenges challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new ChallengeNotFoundException("챌린지를 찾을 수 없습니다."));

        // user 정보 가져오기 (baarer token에서 추출)
        Users user = authenticatedUserUtils.getCurrentUser();

        // 참여했는지 확인
        boolean isAlreadyJoined = challengeUsersRepository.existsByChallengeIdAndUserId(id, user.getId());
        if (isAlreadyJoined) {
            throw new AlreadyJoinedException("이미 참여한 챌린지입니다.");
        }

        int currentParticipants = challengeUsersRepository.countByChallenge_IdAndChallenge_DeletedAtIsNull(id);
        if (currentParticipants >= challenge.getMaxPersonnel()) {
            throw new ChallengeFullException("참여 인원이 가득 찼습니다.");
        }

        ChallengeUsers join = ChallengeUsers.builder()
                .challenge(challenge)
                .user(user)
                .joinedAt(LocalDateTime.now())
                .build();

        challengeChatRoomService.enterChatRoom(challenge, user);

        challengeUsersRepository.save(join);

        ChatRooms chatRooms = chatRoomsRepository.findByChallenge(challenge)
                .orElseThrow(() -> new ChatRoomNotFoundException("해당 채팅방을 찾을 수 없습니다."));

        // 최초 입장 메시지 자동 전송
        challengeMessageService.entreeSendMessage(chatRooms.getRoomId(), user);

        return ChallengeJoinResponse.builder()
                .roomId(chatRooms.getRoomId())
                .build();
    }

    public List<ChallengeListDTO> myChallengeList () {
        // user 정보 가져오기 (baarer token에서 추출)
        Users user = authenticatedUserUtils.getCurrentUser();

        List<ChallengeUsers> joined = challengeUsersRepository.findByUserId(user.getId());

        return joined.stream()
                .map(cu -> {
                    Challenges challenge = cu.getChallenge();
                    ChallengeListDTO dto = new ChallengeListDTO(challenge);

                    dto.setJoined(true);
                    // roomId 세팅
                    chatRoomsRepository.findByChallenge(challenge)
                            .ifPresent(cr -> dto.setRoomId(cr.getRoomId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public void exitChallenge(Long challengeId) {
        Users user = authenticatedUserUtils.getCurrentUser();

        Challenges challenges = challengeRepository.findByIdAndDeletedAtIsNull(challengeId)
                .orElseThrow(() -> new ChallengeNotFoundException("해당 챌린지방을 찾을 수 없습니다."));

        ChallengeUsers challengeUser = challengeUsersRepository
                .findByChallengeIdAndUserId(challengeId, user.getId())
                .orElseThrow(() -> new ChallengeAccessDeniedException("챌린지방 참가자가 아닙니다."));

        challengeUsersRepository.delete(challengeUser);

        ChatRooms chatRooms = chatRoomsRepository.findByChallenge(challenges)
                .orElseThrow(() -> new ChatRoomNotFoundException("해당 채팅방을 찾을 수 없습니다."));

        ChatRoomUsers chatRoomUsers = chatRoomUsersRepository.findByChatRooms_RoomIdAndUsers(chatRooms.getRoomId(), user)
                        .orElseThrow(() -> new ChatRoomAccessDeniedException("해당 채팅방 유저가 아닙니다."));

        // 남은 멤버 조회
        List<ChatRoomUsers> remainUsers =
                chatRoomUsersRepository
                        .findByChatRooms_RoomIdAndUsersNotOrderByCreatedAtAsc(
                                chatRooms.getRoomId(), user);

        if (chatRoomUsers.getChatRoomRole() == ChatRoomRole.HOST) {

            if (!remainUsers.isEmpty()) {
                ChatRoomUsers newHost = remainUsers.get(0);
                newHost.setChatRoomRole(ChatRoomRole.HOST);
                chatRoomUsersRepository.save(newHost);
            } else {
                // 마지막 멤버 → 챌린지방 삭제
                challenges.setDeletedAt(LocalDateTime.now());
                chatRooms.setDeletedAt(LocalDateTime.now());
                challengeRepository.save(challenges);
                chatRoomsRepository.save(chatRooms);
            }
        }

        chatRoomUsersRepository.delete(chatRoomUsers);

        // 채팅방 나가는 메시지 전송
        challengeMessageService.exitSendMessage(chatRooms.getRoomId(), user);
    }



}







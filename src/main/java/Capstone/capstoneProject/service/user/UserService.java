package Capstone.capstoneProject.service.user;

import Capstone.capstoneProject.dto.*;
import Capstone.capstoneProject.dto.board.BoardImageDTO;
import Capstone.capstoneProject.dto.board.BoardLikesDTO;
import Capstone.capstoneProject.dto.board.BoardListDTO;
import Capstone.capstoneProject.dto.challenge.ChallengeListDTO;
import Capstone.capstoneProject.dto.comment.CommentListDTO;
import Capstone.capstoneProject.entity.board.BoardLikes;
import Capstone.capstoneProject.entity.board.Boards;
import Capstone.capstoneProject.entity.comment.Comments;
import Capstone.capstoneProject.entity.user.UserProfile;
import Capstone.capstoneProject.entity.user.Users;
import Capstone.capstoneProject.entity.challenge.ChallengeLikes;
import Capstone.capstoneProject.entity.challenge.Challenges;
import Capstone.capstoneProject.enums.SortType;
import Capstone.capstoneProject.enums.UserRole;
import Capstone.capstoneProject.exceptions.unauthorized.PasswordMismatchException;
import Capstone.capstoneProject.exceptions.conflict.UserAlreadyExistsException;
import Capstone.capstoneProject.exceptions.notfound.UserNotFoundException;
import Capstone.capstoneProject.repository.*;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final AuthenticatedUserUtils authenticatedUserUtils;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final BoardLikeRepository boardLikeRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final ChatRoomsRepository chatRoomsRepository;
    private final ChallengeUsersRepository challengeUsersRepository;

    //회원가입
    public void signup(SecuritySignupRequest request) {
        if(userRepository.findByEmailAndDeletedAtIsNull(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("이미 존재하는 계정입니다.");
        }
        if (!request.getPassword().equals(request.getPasswordCheck())) {
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        Users user = Users.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.USER)
                .provider("local")
                .providerId(request.getEmail()) // 고유 Id
                .build();
        userRepository.save(user);

        UserProfile profile = UserProfile.builder()
                .nickname(request.getNickname())
                .statusMessage(request.getStatusMessage())
                .profileImg(request.getProfileImg())
                .user(user).build();

        userProfileRepository.save(profile);
    }

    // 마이프로필 조회
    public UserResponseDTO getMyProfile() {
        // user 정보 가져오기 (baarer token에서 추출)
        Users user = authenticatedUserUtils.getCurrentUser();
        // userProfile 정보 가져오기
        UserProfile profile = userProfileRepository.findByUserId(user.getId());
        // 엔티티 → DTO 변환
        UserResponseDTO result = new UserResponseDTO(user, profile);
        return result;
    }

    // 마이프로필 수정
    public ProfilePatchDTO patchMyProfile(ProfilePatchDTO dto) {
        // user 정보 가져오기 (baarer token에서 추출)
        Users user = authenticatedUserUtils.getCurrentUser();
        // userProfile 정보 가져오기
        UserProfile profile = userProfileRepository.findByUserId(user.getId());

        profile.setNickname(dto.getNickname());
        profile.setProfileImg(dto.getProfileImg());
        profile.setStatusMessage(dto.getStatusMessage());
        // db에 저장
        userRepository.save(user);
        userProfileRepository.save(profile);
        ProfilePatchDTO result = new ProfilePatchDTO(user, profile);
        return result;
    }

    public void patchMyPassword(PasswordPatchDTO dto) {
        Users user = authenticatedUserUtils.getCurrentUser();
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }


    @Transactional
    public UserDeleteDTO deleteUser() {
        // user 정보 가져오기 (baarer token에서 추출)
        Users user = authenticatedUserUtils.getCurrentUser();
        UserProfile profile = userProfileRepository.findByUserId(user.getId());
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);

        return UserDeleteDTO.builder()
                .email(user.getEmail())
                .nickname(profile.getNickname())
                .profileImg(profile.getProfileImg())
                .deletedAt(user.getDeletedAt())
                .build();
    }

    public List<BoardListDTO> getMyLikeBoards(SortType sortType) {
        Users user = authenticatedUserUtils.getCurrentUser();
        List<BoardLikes> boardLikes;
        // 기본값
        SortType finalSort = (sortType == null) ? SortType.RECENT : sortType;

        if (finalSort == SortType.RECENT) {
            boardLikes = boardLikeRepository
                    .findByUsersAndBoards_DeletedAtIsNullOrderByCreatedAtDesc(user);
        } else if (finalSort == SortType.OLDEST) {
            boardLikes = boardLikeRepository
                    .findByUsersAndBoards_DeletedAtIsNullOrderByCreatedAtAsc(user);
        } else if (finalSort == SortType.POPULAR) {
            boardLikes = boardLikeRepository
                    .findByUsersAndBoards_DeletedAtIsNullOrderByBoards_LikeCountDescCreatedAtDesc(user);
        } else {
            boardLikes = boardLikeRepository
                    .findByUsersAndBoards_DeletedAtIsNullOrderByCreatedAtDesc(user);
        }

        return boardLikes.stream().map(like -> {
            Boards board = like.getBoards();
            return BoardListDTO.builder()
                    .id(board.getId())
                    .user(UserSummaryDTO.from(board.getUsers()))
                    .title(board.getTitle())
                    .category(board.getCategory())
                    .content(board.getContent())
                    .likes(BoardLikesDTO.builder()
                            .likeCount(board.getLikeCount())
                            .isLiked(true)
                            .build())
                    .commentCount(board.getCommentCount())
                    .createdAt(board.getCreatedAt())
                    .image(BoardImageDTO.from(board))
                    .build();
        }).collect(Collectors.toList());
    }

    public List<BoardListDTO> getMyBoards(SortType sortType) {
        Users user = authenticatedUserUtils.getCurrentUser();
        // 기본값
        SortType finalSort = (sortType == null) ? SortType.RECENT : sortType;

        List<Boards> boards;
        if (finalSort == SortType.RECENT) {
            // 최신순
            boards = boardRepository.findByUsersAndDeletedAtIsNullOrderByCreatedAtDesc(user);
        } else if (finalSort == SortType.OLDEST) {
            // 오래된순
            boards = boardRepository.findByUsersAndDeletedAtIsNullOrderByCreatedAtAsc(user);
        } else {
            // 기본 최신순
            boards = boardRepository.findByUsersAndDeletedAtIsNullOrderByCreatedAtDesc(user);
        }
        List<Long> boardIds = boards.stream().map(Boards::getId).collect(Collectors.toList());
        Set<Long> likedIds = boards.isEmpty()
                ? Collections.emptySet()
                : boardLikeRepository.findLikedBoardIdsByUserAndBoardIds(user, boardIds);

        return boards.stream().map(board -> BoardListDTO.builder()
                .id(board.getId())
                .user(UserSummaryDTO.from(board.getUsers()))
                .title(board.getTitle())
                .category(board.getCategory())
                .content(board.getContent())
                .likes(BoardLikesDTO.builder()
                        .likeCount(board.getLikeCount())
                        .isLiked(likedIds.contains(board.getId()))
                        .build())
                .commentCount(board.getCommentCount())
                .createdAt(board.getCreatedAt())
                .image(BoardImageDTO.from(board))
                .build()).collect(Collectors.toList());
    }

    public UserSummaryDTO getProfile(Long userId) {
        Users user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 유저입니다."));
        return UserSummaryDTO.from(user);
    }

    public List<CommentListDTO> getMyComments(SortType sortType) {
        Users user = authenticatedUserUtils.getCurrentUser();

        List<Comments> comments;
        // 기본값
        SortType finalSort = (sortType == null) ? SortType.RECENT : sortType;

        if (finalSort == SortType.OLDEST) {
            comments = commentRepository.findByUsersOrderByCreatedAtAsc(user);
        } else if (finalSort == SortType.POPULAR) {
            comments = commentRepository.findByUsersOrderByLikeCountDescCreatedAtDesc(user);
        } else {
            comments = commentRepository.findByUsersOrderByCreatedAtDesc(user);
        }
        return CommentListDTO.fromComments(comments, user);
    }

    // 내가 좋아요한 챌린지방 조회
    public List<ChallengeListDTO> getMyLikeChallengeList() {
        // user 정보 가져오기 (baarer token에서 추출)
        Users user = authenticatedUserUtils.getCurrentUser();
        List<ChallengeLikes> likes = likeRepository.findAllByUserOrderByCreatedAtDesc(user);
        List<Long> challengeIds = likes.stream()
                .map(l -> l.getChallenges().getId()).collect(Collectors.toList());
        Set<Long> joinedIds = challengeIds.isEmpty()
                ? Collections.emptySet()
                : challengeUsersRepository.findJoinedChallengeIdsByUserIdAndChallengeIds(user.getId(), challengeIds);
        List<Long> joinedChallengeIds = new ArrayList<>(joinedIds);
        Map<Long, String> roomIdMap = joinedChallengeIds.isEmpty()
                ? Collections.emptyMap()
                : chatRoomsRepository.findByChallengeIds(joinedChallengeIds).stream()
                        .collect(Collectors.toMap(cr -> cr.getChallenge().getId(), cr -> cr.getRoomId()));

        return likes.stream()
                .map(like -> {
                    Challenges challenge = like.getChallenges();
                    ChallengeListDTO dto = new ChallengeListDTO(challenge);
                    boolean isJoined = joinedIds.contains(challenge.getId());
                    dto.setJoined(isJoined);
                    if (isJoined) {
                        String roomId = roomIdMap.get(challenge.getId());
                        if (roomId != null) dto.setRoomId(roomId);
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }
}

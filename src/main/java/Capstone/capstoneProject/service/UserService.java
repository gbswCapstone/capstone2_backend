package Capstone.capstoneProject.service;

import Capstone.capstoneProject.dto.*;
import Capstone.capstoneProject.dto.Boards.BoardImageDTO;
import Capstone.capstoneProject.dto.Boards.BoardLikesDTO;
import Capstone.capstoneProject.dto.Boards.BoardListDTO;
import Capstone.capstoneProject.dto.Challenges.ChallengeListDTO;
import Capstone.capstoneProject.dto.Comments.CommentListDTO;
import Capstone.capstoneProject.entity.Boards.BoardLikes;
import Capstone.capstoneProject.entity.Boards.Boards;
import Capstone.capstoneProject.entity.Comments;
import Capstone.capstoneProject.entity.UserProfile;
import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.entity.challenges.ChallengeLikes;
import Capstone.capstoneProject.entity.challenges.Challenges;
import Capstone.capstoneProject.enums.SortType;
import Capstone.capstoneProject.enums.UserRole;
import Capstone.capstoneProject.exceptions.PasswordMismatchException;
import Capstone.capstoneProject.exceptions.UserAlreadyExistsException;
import Capstone.capstoneProject.repository.*;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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

        List<Boards> boards = boardLikes.stream()
                .map(BoardLikes::getBoards)
                .collect(Collectors.toList());

        return boards.stream().map(board -> {
                boolean isLiked = boardLikeRepository.existsByUsersAndBoardsAndBoards_DeletedAtIsNull(user, board); // 내가 눌렀는지 여부
            return BoardListDTO.builder()
                    .id(board.getId())
                    .user(UserSummaryDTO.from(board.getUsers()))
                    .title(board.getTitle())
                    .category(board.getCategory())
                    .content(board.getContent())
                    .likes(BoardLikesDTO.builder()
                            .likeCount(board.getLikeCount())
                            .isLiked(isLiked)
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
        return boards.stream().map(board -> {
            boolean isLiked = boardLikeRepository.existsByUsersAndBoardsAndBoards_DeletedAtIsNull(user, board); // 내가 눌렀는지 여부

            return BoardListDTO.builder()
                    .id(board.getId())
                    .user(UserSummaryDTO.from(board.getUsers()))
                    .title(board.getTitle())
                    .category(board.getCategory())
                    .content(board.getContent())
                    .likes(BoardLikesDTO.builder()
                            .likeCount(board.getLikeCount())
                            .isLiked(isLiked)
                            .build())
                    .commentCount(board.getCommentCount())
                    .createdAt(board.getCreatedAt())
                    .image(BoardImageDTO.from(board))
                    .build();
        }).collect(Collectors.toList());
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
        // 유저가 좋아요한 챌린지 목록
        List<ChallengeLikes> likes = likeRepository.findAllByUserOrderByCreatedAtDesc(user);


        return likes.stream()
                .map(like -> {
                    Challenges challenge = like.getChallenges();
                    ChallengeListDTO dto = new ChallengeListDTO(challenge);
                    // 현재 참여 여부
                    boolean isJoined = challenge.getChallengeUsers().stream()
                            .anyMatch(cu -> cu.getUser().getId().equals(user.getId()));
                    dto.setJoined(isJoined);

                    if (isJoined) {
                        chatRoomsRepository.findByChallenge(challenge)
                                .ifPresent(cr -> dto.setRoomId(cr.getRoomId()));
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }
}

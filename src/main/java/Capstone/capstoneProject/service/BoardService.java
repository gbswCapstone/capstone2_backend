package Capstone.capstoneProject.service;

import Capstone.capstoneProject.dto.Boards.*;
import Capstone.capstoneProject.dto.UserSummaryDTO;
import Capstone.capstoneProject.entity.Boards.BoardImages;
import Capstone.capstoneProject.entity.Boards.BoardLikes;
import Capstone.capstoneProject.entity.Boards.BoardVideos;
import Capstone.capstoneProject.entity.Boards.Boards;
import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.enums.BoardCategory;
import Capstone.capstoneProject.enums.SortType;
import Capstone.capstoneProject.exceptions.*;
import Capstone.capstoneProject.repository.BoardImageRepository;
import Capstone.capstoneProject.repository.BoardLikeRepository;
import Capstone.capstoneProject.repository.BoardRepository;
import Capstone.capstoneProject.repository.BoardVideoRepository;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
    private final AuthenticatedUserUtils authenticatedUserUtils;
    private final BoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;
    private final BoardVideoRepository boardVideoRepository;
    private final BoardLikeRepository boardLikeRepository;

    public BoardResponse createBoard(CreateBoard request) {
        Users user = authenticatedUserUtils.getCurrentUser();

        Boards boards = Boards.builder()
                .users(user)
                .title(request.getTitle())
                .content(request.getContent())
                .category(request.getCategory())
                .likeCount(0)
                .commentCount(0)
                .build();
        boardRepository.save(boards);

        // 이미지 변환 & 저장
        if (request.getImagesList() != null && !request.getImagesList().isEmpty()) {
            List<BoardImages> imagesList = request.getImagesList().stream()
                    .map(img -> BoardImages.builder()
                            .boards(boards)
                            .imageUrl(img.getImageUrl())
                            .build())
                    .collect(Collectors.toList());

            boardImageRepository.saveAll(imagesList);
            boards.getBoardImages().addAll(imagesList);
        }


        // 비디오 변환 & 저장
        if (request.getVideosList() != null && !request.getVideosList().isEmpty()) {
            List<BoardVideos> videosList = request.getVideosList().stream()
                    .map(vid -> BoardVideos.builder()
                            .boards(boards)
                            .videoUrl(vid.getVideoUrl())
                            .build())
                    .collect(Collectors.toList());
            boardVideoRepository.saveAll(videosList);
        }

        return BoardResponse.from(boards);
    }


    public BoardDetailResponse getBoard(Long id) {
        Boards board = boardRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new BoardNotFoundException("해당 게시물을 찾을 수 없습니다."));
        Users user = authenticatedUserUtils.getCurrentUser();
        boolean isLiked = boardLikeRepository.existsByUsersAndBoardsAndBoards_DeletedAtIsNull(user, board);
        return BoardDetailResponse.from(board, user, board.getLikeCount(), isLiked);
    }


    public List<BoardListDTO> getBoards(SortType sortType, BoardCategory boardCategory) {
        Users users = authenticatedUserUtils.getCurrentUser();
        // 기본값 처리
        SortType finalSort = (sortType == null) ? SortType.RECENT : sortType;
        BoardCategory finalCategory = (boardCategory == null) ? BoardCategory.NONE : boardCategory;

        List<Boards> boards;

        // sortType에 따른 조회
        if (finalCategory == BoardCategory.NONE) {
            if (finalSort == SortType.POPULAR) {
                boards = boardRepository.findAllByDeletedAtIsNullOrderByLikeCountDescCreatedAtDesc();
            } else if (finalSort == SortType.OLDEST) {
                boards = boardRepository.findAllByDeletedAtIsNullOrderByCreatedAtAsc();
            } else { // RECENT
                boards = boardRepository.findAllByDeletedAtIsNullOrderByCreatedAtDesc();
            }
        } else { // 특정 카테고리 있는 경우
            if (finalSort == SortType.POPULAR) {
                boards = boardRepository.findAllByCategoryAndDeletedAtIsNullOrderByLikeCountDescCreatedAtDesc(finalCategory);
            } else if (finalSort == SortType.OLDEST) {
                boards = boardRepository.findAllByCategoryAndDeletedAtIsNullOrderByCreatedAtAsc(finalCategory);
            } else { // RECENT
                boards = boardRepository.findAllByCategoryAndDeletedAtIsNullOrderByCreatedAtDesc(finalCategory);
            }
        }

        return boards.stream().map(board -> {
            boolean isLiked = boardLikeRepository.existsByUsersAndBoardsAndBoards_DeletedAtIsNull(users, board); // 내가 눌렀는지 여부

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


    public BoardResponse patchBoard(CreateBoard request, Long id) {
        Boards board = boardRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new BoardNotFoundException("해당 게시물을 찾을 수 없습니다."));

        Users user = authenticatedUserUtils.getCurrentUser();

        if (!board.getUsers().getId().equals(user.getId())) {
            throw new NotBoardOwnerException("게시글을 수정할 권한이 없습니다.");
        }
        board.setTitle(request.getTitle());
        board.setContent(request.getContent());
        board.setCategory(request.getCategory());
        board.setBoardImages(request.getImagesList());
        board.setBoardVideos(request.getVideosList());
        boardRepository.save(board);

        // 이미지 변환 & 저장
        if (request.getImagesList() != null && !request.getImagesList().isEmpty()) {
            boardImageRepository.deleteByBoards(board);
            List<BoardImages> imagesList = request.getImagesList().stream()
                    .map(img -> BoardImages.builder()
                            .boards(board)
                            .imageUrl(img.getImageUrl())
                            .build())
                    .collect(Collectors.toList());

            boardImageRepository.saveAll(imagesList);
        }

        // 비디오 변환 & 저장
        if (request.getVideosList() != null && !request.getVideosList().isEmpty()) {
            List<BoardVideos> videosList = request.getVideosList().stream()
                    .map(vid -> BoardVideos.builder()
                            .boards(board)
                            .videoUrl(vid.getVideoUrl())
                            .build())
                    .collect(Collectors.toList());
            boardVideoRepository.saveAll(videosList);
        }
        return BoardResponse.from(board);
    }


    public BoardDeleteResponse deleteBoard(Long id) {
        Users user = authenticatedUserUtils.getCurrentUser();

        Boards board = boardRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new BoardNotFoundException("해당 게시물을 찾을 수 없습니다."));

        if (!board.getUsers().getId().equals(user.getId())) {
            throw new NotBoardOwnerException("해당 게시물을 삭제할 수 있는 권한이 없습니다.");
        }

        board.setDeletedAt(LocalDateTime.now());
        boardRepository.save(board);
        return BoardDeleteResponse.from(board);
    }

    public List<BoardListDTO> searchBoard(String keyword, SortType sortType, BoardCategory category) {
        Users user = authenticatedUserUtils.getCurrentUser();
        // 검색어 없으면 빈 리스트
        if (keyword == null || keyword.isBlank()) {
            return Collections.emptyList();
        }

        // 기본값 처리
        SortType finalSort = (sortType == null) ? SortType.RECENT : sortType;
        BoardCategory finalCategory = (category == null) ? BoardCategory.NONE : category;

        List<Boards> boards;

        if (finalCategory == BoardCategory.NONE) {
            if (finalSort == SortType.POPULAR) {
                boards = boardRepository
                        .findAllByDeletedAtIsNullAndTitleContainingIgnoreCaseOrderByLikeCountDescCreatedAtDesc(keyword);
            } else if (finalSort == SortType.OLDEST) {
                boards = boardRepository
                        .findAllByDeletedAtIsNullAndTitleContainingIgnoreCaseOrderByCreatedAtAsc(keyword);
            } else { // RECENT
                boards = boardRepository
                        .findAllByDeletedAtIsNullAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(keyword);
            }
        } else { // 카테고리 검색
            if (finalSort == SortType.POPULAR) {
                boards = boardRepository
                        .findAllByDeletedAtIsNullAndCategoryAndTitleContainingIgnoreCaseOrderByLikeCountDescCreatedAtDesc(
                                finalCategory, keyword);
            } else if (finalSort == SortType.OLDEST) {
                boards = boardRepository
                        .findAllByDeletedAtIsNullAndCategoryAndTitleContainingIgnoreCaseOrderByCreatedAtAsc(
                                finalCategory, keyword);
            } else { // RECENT
                boards = boardRepository
                        .findAllByDeletedAtIsNullAndCategoryAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(
                                finalCategory, keyword);
            }
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

    public BoardLikesDTO likeBoard(Long id) {
        Users user = authenticatedUserUtils.getCurrentUser();

        Boards board = boardRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new BoardNotFoundException("해당 게시물을 찾을 수 없습니다."));

        boolean isLiked = boardLikeRepository.existsByUsersAndBoardsAndBoards_DeletedAtIsNull(user,board);

        if (isLiked) {
            BoardLikes boardLikes = boardLikeRepository.findByUsersAndBoardsAndBoards_DeletedAtIsNull(user, board)
                    .orElseThrow(() -> new BoardLikeNotFoundException("해당 게시글의 좋아요 데이터가 존재하지 않습니다."));

            boardLikeRepository.delete(boardLikes);
            board.setLikeCount(board.getLikeCount() - 1);

        } else {
            BoardLikes newLike = BoardLikes.builder()
                    .boards(board)
                    .users(user)
                    .build();

            boardLikeRepository.save(newLike);
            board.setLikeCount(board.getLikeCount() + 1);
        }
        boardRepository.save(board);

        return BoardLikesDTO.from(board, user, board.getLikeCount(), !isLiked);
    }
}

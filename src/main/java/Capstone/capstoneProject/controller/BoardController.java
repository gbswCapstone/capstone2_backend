package Capstone.capstoneProject.controller;

import Capstone.capstoneProject.dto.Boards.*;
import Capstone.capstoneProject.enums.BoardCategory;
import Capstone.capstoneProject.enums.SortType;
import Capstone.capstoneProject.global.ApiResponse;
import Capstone.capstoneProject.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping
    @Operation(summary = "게시물 생성", description = "게시물 생성 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<BoardResponse>> createBoard
            (@RequestBody CreateBoard request) {
        BoardResponse result = boardService.createBoard(request);
        return ResponseEntity.ok(ApiResponse.ok(result, "생성 되었습니다."));
    }

    @GetMapping("/{id}")
    @Operation(summary = "게시물 상세 조회", description = "게시물 상세 조회 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<BoardDetailResponse>> getBoard
            (@PathVariable Long id) {
        BoardDetailResponse result = boardService.getBoard(id);
        return ResponseEntity.ok(ApiResponse.ok(result, "조회되었습니다."));
    }

    @GetMapping
    @Operation(summary = "게시물 전체 조회", description = "게시물 전체 조회 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<List<BoardListDTO>>> getBoardList
            (@RequestParam(required = false) SortType sortType,
             @RequestParam(required = false) BoardCategory category) {
        List<BoardListDTO> result = boardService.getBoards(sortType, category);
        return ResponseEntity.ok(ApiResponse.ok(result, "조회되었습니다."));
    }

    @PutMapping("/{id}")
    @Operation(summary = "게시물 수정", description = "게시물 수정 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<BoardResponse>> patchBoard
            (@PathVariable Long id, @RequestBody CreateBoard request) {
        BoardResponse result = boardService.patchBoard(request, id);
        return ResponseEntity.ok(ApiResponse.ok(result, "수정되었습니다."));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "게시물 삭제",description = "게시물 삭제 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<BoardDeleteResponse>> deleteBoard
            (@PathVariable Long id) {
        BoardDeleteResponse result = boardService.deleteBoard(id);
        return ResponseEntity.ok(ApiResponse.ok(result, "삭제되었습니다."));
    }

    @GetMapping("/search")
    @Operation(summary = "게시물 검색", description = "게시물 검색 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<List<BoardListDTO>>> searchBoard
            (String keyword, @RequestParam(required = false) SortType sortType,
             @RequestParam(required = false) BoardCategory category
             ) {
        List<BoardListDTO> result = boardService.searchBoard(keyword, sortType, category);
        return ResponseEntity.ok(ApiResponse.ok(result, "검색되었습니다."));
    }

    @PostMapping("/likes/{id}")
    @Operation(summary = "게시글 좋아요 추가&취소", description = "게시물 좋아요 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<BoardLikesDTO>> likeBoard(@PathVariable Long id) {
        BoardLikesDTO result = boardService.likeBoard(id);
        return ResponseEntity.ok(ApiResponse.ok(result, "처리되었습니다."));
    }


}

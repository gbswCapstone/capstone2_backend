package Capstone.capstoneProject.controller;

import Capstone.capstoneProject.dto.Comments.CommentListDTO;
import Capstone.capstoneProject.dto.Comments.CommentResponse;
import Capstone.capstoneProject.dto.Comments.CreateComment;
import Capstone.capstoneProject.dto.Comments.PatchComment;
import Capstone.capstoneProject.dto.LikeResponseDTO;
import Capstone.capstoneProject.enums.SortType;
import Capstone.capstoneProject.global.ApiResponse;
import Capstone.capstoneProject.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{boardId}")
    @Operation(summary = "댓글 생성", description = "댓글 생성 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment
            (@PathVariable Long boardId, @RequestBody CreateComment request) {
        CommentResponse result = commentService.createComment(request, boardId);
        return ResponseEntity.ok(ApiResponse.ok(result, "생성되었습니다."));
    }

    @GetMapping("/{boardId}")
    @Operation(summary = "게시물의 댓글 전체 조회", description = "게시물 댓글 전체 조회 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<List<CommentListDTO>>> getComments
            (@PathVariable Long boardId,
             @RequestParam(required = false) SortType sortType) {
        List<CommentListDTO> result = commentService.getComments(boardId, sortType);
        return ResponseEntity.ok(ApiResponse.ok(result, "조회되었습니다."));
    }


    @PutMapping("/{id}")
    @Operation(summary = "댓글 수정", description = "댓글 수정 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<CommentResponse>> patchComment
            (@PathVariable Long id, @RequestBody PatchComment request) {
        CommentResponse result = commentService.patchComment(request, id);
        return ResponseEntity.ok(ApiResponse.ok(result, "수정되었습니다."));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "댓글 삭제", description = "댓글 삭제 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable Long id){
        commentService.deleteComment(id);
        return ResponseEntity.ok(ApiResponse.ok("삭제되었습니다."));
    }

    @PostMapping("/likes/{id}")
    @Operation(summary = "댓글 좋아요", description = "댓글 좋아요 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<LikeResponseDTO>> likeComment
            (@PathVariable Long id) {
        LikeResponseDTO result = commentService.likeComment(id);
        return ResponseEntity.ok(ApiResponse.ok(result, "처리되었습니다."));
    }
}

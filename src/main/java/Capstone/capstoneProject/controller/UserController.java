package Capstone.capstoneProject.controller;

import Capstone.capstoneProject.dto.*;
import Capstone.capstoneProject.dto.Boards.BoardListDTO;
import Capstone.capstoneProject.dto.Comments.CommentListDTO;
import Capstone.capstoneProject.enums.SortType;
import Capstone.capstoneProject.global.ApiResponse;
import Capstone.capstoneProject.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "회원가입", description = "회원가입 시 사용하는 API 입니다.")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Object>> signup(@RequestBody SecuritySignupRequest request) {
        userService.signup(request);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @DeleteMapping("/quit")
    @Operation(summary = "회원탈퇴", description = "회원탈퇴 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<UserDeleteDTO>> deleteUser() {
        UserDeleteDTO result = userService.deleteUser();
        return ResponseEntity.ok(ApiResponse.ok(result, "삭제되었습니다."));
    }

    @GetMapping("/profile")
    @Operation(summary = "내 프로필 조회하기", description = "프로필 조회 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<UserResponseDTO>> myProfile() {
        UserResponseDTO result = userService.getMyProfile();
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @PutMapping("/profile")
    @Operation(summary = "내 프로필 수정하기", description = "프로필 수정 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<ProfilePatchDTO>> myProfilePatch(
            @RequestBody ProfilePatchDTO dto
    ) {
        ProfilePatchDTO result = userService.patchMyProfile(dto);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @PutMapping("/password")
    @Operation(summary = "내 비밀번호 수정하기", description = "비밀번호 수정 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<Void>> myPasswordPatch(@RequestBody PasswordPatchDTO dto) {
        userService.patchMyPassword(dto);
        return ResponseEntity.ok(ApiResponse.ok("비밀번호가 변경되었습니다."));
    }

    @GetMapping("/likeBoards")
    @Operation(summary = "좋아요한 게시글 조회", description = "좋아요한 게시글 조회 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<List<BoardListDTO>>> myLikeBoards
            (@RequestParam(required = false) SortType sortType) {
        List<BoardListDTO> result = userService.getMyLikeBoards(sortType);
        return ResponseEntity.ok(ApiResponse.ok(result, "조회되었습니다."));
    }

    @GetMapping("/myBoards")
    @Operation(summary = "내 게시글 조회", description = "내 게시글 조회 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<List<BoardListDTO>>> getMyBoards
            (@RequestParam(required = false) SortType sortType) {
        List<BoardListDTO> result = userService.getMyBoards(sortType);
        return ResponseEntity.ok(ApiResponse.ok(result, "조회되었습니다."));
    }

    @GetMapping("/myComments")
    @Operation(summary = "내 댓글 조회", description = "내 댓글 조회 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<List<CommentListDTO>>> myComments
            (@RequestParam(required = false)    SortType sortType) {
        List<CommentListDTO> result = userService.getMyComments(sortType);
        return ResponseEntity.ok(ApiResponse.ok(result, "조회되었습니다."));
    }



}

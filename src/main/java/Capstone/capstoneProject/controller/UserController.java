package Capstone.capstoneProject.controller;

import Capstone.capstoneProject.dto.*;
import Capstone.capstoneProject.dto.Boards.BoardListDTO;
import Capstone.capstoneProject.dto.Challenges.ChallengeListDTO;
import Capstone.capstoneProject.dto.Comments.CommentListDTO;
import Capstone.capstoneProject.enums.SortType;
import Capstone.capstoneProject.global.ApiResponse;
import Capstone.capstoneProject.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409", description = "이미 존재하는 계정입니다.",
            content = @Content)
    })
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Object>> signup(@RequestBody SecuritySignupRequest request) {
        userService.signup(request);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @DeleteMapping("/quit")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403", description = "로그인이 필요합니다.",
            content = @Content)
    })
    @Operation(summary = "회원탈퇴", description = "회원탈퇴 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<UserDeleteDTO>> deleteUser() {
        UserDeleteDTO result = userService.deleteUser();
        return ResponseEntity.ok(ApiResponse.ok(result, "삭제되었습니다."));
    }

    @GetMapping("/my-profile")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403", description = "로그인이 필요합니다.",
            content = @Content)
    })
    @Operation(summary = "내 프로필 조회하기", description = "프로필 조회 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<UserResponseDTO>> myProfile() {
        UserResponseDTO result = userService.getMyProfile();
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @GetMapping("/profile/{userId}")
    @Operation(summary = "프로필 조회", description = "프로필 조회 시 사용하는 API 입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404", description = "존재하지 않는 유저입니다.",
            content = @Content),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403", description = "로그인이 필요합니다.",
            content = @Content)
    })
    public ResponseEntity<ApiResponse<UserSummaryDTO>> getProfile(@PathVariable Long userId) {
        UserSummaryDTO result = userService.getProfile(userId);
        return ResponseEntity.ok(ApiResponse.ok(result, "조회되었습니다."));
    }


    @PutMapping("/profile")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403", description = "로그인이 필요합니다.",
            content = @Content)
    })
    @Operation(summary = "내 프로필 수정하기", description = "프로필 수정 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<ProfilePatchDTO>> myProfilePatch(
            @RequestBody ProfilePatchDTO dto
    ) {
        ProfilePatchDTO result = userService.patchMyProfile(dto);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @PutMapping("/password")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401", description = "비밀번호가 일치하지 않습니다.",
            content = @Content),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403", description = "로그인이 필요합니다.",
            content = @Content)
    })
    @Operation(summary = "내 비밀번호 수정하기", description = "비밀번호 수정 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<Void>> myPasswordPatch(@RequestBody PasswordPatchDTO dto) {
        userService.patchMyPassword(dto);
        return ResponseEntity.ok(ApiResponse.ok("비밀번호가 변경되었습니다."));
    }

    @GetMapping("/likes/boards")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403", description = "로그인이 필요합니다.",
                    content = @Content)
    })
    @Operation(summary = "좋아요한 게시글 조회", description = "좋아요한 게시글 조회 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<List<BoardListDTO>>> myLikeBoards
            (@RequestParam(required = false) SortType sortType) {
        List<BoardListDTO> result = userService.getMyLikeBoards(sortType);
        return ResponseEntity.ok(ApiResponse.ok(result, "조회되었습니다."));
    }

    @GetMapping("/likes/challenges")
    @Operation(
            summary = "좋아요한 챌린지 전체조회",
            description = "내가 좋아요한 챌린지 전체 조회 시 사용하는 API 입니다.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "요청 성공"
                    ),  @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "로그인이 필요합니다.",
                    content = @Content
            )
            }
    )
    public ResponseEntity<ApiResponse<List<ChallengeListDTO>>> getMyLikeChallenge() {
        List<ChallengeListDTO> result = userService.getMyLikeChallengeList();
        return ResponseEntity.ok(ApiResponse.ok(result));
    }




    @GetMapping("/myBoards")
    @Operation(summary = "내 게시글 조회", description = "내 게시글 조회 시 사용하는 API 입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403", description = "로그인이 필요합니다.",
                    content = @Content)
    })
    public ResponseEntity<ApiResponse<List<BoardListDTO>>> getMyBoards
            (@RequestParam(required = false) SortType sortType) {
        List<BoardListDTO> result = userService.getMyBoards(sortType);
        return ResponseEntity.ok(ApiResponse.ok(result, "조회되었습니다."));
    }

    @GetMapping("/myComments")
    @Operation(summary = "내 댓글 조회", description = "내 댓글 조회 시 사용하는 API 입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403", description = "로그인이 필요합니다.",
                    content = @Content)
    })
    public ResponseEntity<ApiResponse<List<CommentListDTO>>> myComments
            (@RequestParam(required = false)    SortType sortType) {
        List<CommentListDTO> result = userService.getMyComments(sortType);
        return ResponseEntity.ok(ApiResponse.ok(result, "조회되었습니다."));
    }



}

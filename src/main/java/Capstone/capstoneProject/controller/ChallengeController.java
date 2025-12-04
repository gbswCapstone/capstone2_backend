package Capstone.capstoneProject.controller;

import Capstone.capstoneProject.dto.*;
import Capstone.capstoneProject.dto.Challenges.ChallengeCreate;
import Capstone.capstoneProject.dto.Challenges.ChallengeDetailResponse;
import Capstone.capstoneProject.dto.Challenges.ChallengeListDTO;
import Capstone.capstoneProject.entity.challenges.Challenges;
import Capstone.capstoneProject.enums.SortType;
import Capstone.capstoneProject.enums.UserJobs;
import Capstone.capstoneProject.global.ApiResponse;
import Capstone.capstoneProject.service.ChallengeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/challenges")
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeService challengeService;

    @PostMapping
    @Operation(summary = "챌린지 생성", description = "챌린지방 신규 생성 시 사용하는 API 입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401", description = "로그인이 필요합니다.")
    })
    public ResponseEntity<ApiResponse<ChallengeDetailResponse>> createChallenge(@RequestBody ChallengeCreate dto) {
        ChallengeDetailResponse result = challengeService.create(dto);
        return ResponseEntity.ok(ApiResponse.ok(result, "생성되었습니다."));
    }

    @GetMapping
    @Operation(summary = "챌린지 전체조회", description = "챌린지방 전체 조회 시 사용하는 API 입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401", description = "로그인이 필요합니다.")
    })
    public ResponseEntity<ApiResponse<List<ChallengeListDTO>>> getChallengeList(
            @Parameter(description = "정렬", schema = @Schema(allowableValues = {"최신순", "오래된순", "인기순"}))
            @RequestParam(required = false) SortType sortType,
            @RequestParam(required = false) UserJobs job
    ) {
        List<ChallengeListDTO> result = challengeService.getChallengeList(sortType, job);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }


    @GetMapping("/{id}")
    @Operation(summary = "챌린지 단일조회", description = "챌린지방 단일 조회 시 사용하는 API 입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404", description = "해당 챌린지방을 찾을 수 없습니다."),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401", description = "로그인이 필요합니다.")
    })
    public ResponseEntity<ApiResponse<ChallengeDetailResponse>> getChallenge(@PathVariable Long id) {
        ChallengeDetailResponse result = challengeService.getChallenge(id);
        return ResponseEntity.ok(ApiResponse.ok(result, "조회되었습니다."));
    }

    @PostMapping("/like/{id}")
    @Operation(summary = "챌린지방 좋아요/취소", description = "챌린지방 좋아요 or 좋아요 취소 시 사용하는 API 입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404", description = "해당 챌린지방을 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401", description = "로그인이 필요합니다.")
    })
    public ResponseEntity<ApiResponse<LikeResponseDTO>> likeOrUnlike(@PathVariable Long id) {
        LikeResponseDTO result = challengeService.toggleLike(id);
        return ResponseEntity.ok(ApiResponse.ok(result, "처리되었습니다."));
    }

    @PutMapping("/{id}")
    @Operation(summary = "챌린지방 수정", description = "등록되어 있는 기존 챌린지방 수정 시 사용하는 API 입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404", description = "해당 챌린지방을 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403", description = "해당 챌린지방에 관한 권한이 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401", description = "로그인이 필요합니다.")
    })
    public ResponseEntity<ApiResponse<ChallengeDetailResponse>> updateChallenge(
            @PathVariable Long id,
            @RequestBody ChallengeCreate request) {
        ChallengeDetailResponse result = challengeService.update(id, request);
        return ResponseEntity.ok(ApiResponse.ok(result, "수정되었습니다."));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "챌린지방 삭제", description =  "등록되어 있는 기존 챌린지방 삭제 시 사용하는 API 입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404", description = "해당 챌린지방을 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403", description = "해당 챌린지방에 관한 권한이 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401", description = "로그인이 필요합니다.")
    })
    public ResponseEntity<ApiResponse<Void>> deleteChallenge(
            @PathVariable Long id
    ) {
        challengeService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("삭제되었습니다."));
    }

    @GetMapping("/search")
    @Operation(summary = "챌린지 방 검색", description =  "해시태그, 방 제목으로 검색할 수 있는 API 입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401", description = "로그인이 필요합니다.")
    })
    public ResponseEntity<ApiResponse<List<ChallengeListDTO>>> searchHashtagChallenges(
            @RequestParam(required = false) String hashtag,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) SortType sortType,
            @RequestParam(required = false) UserJobs userJobs
            ) {
        List<ChallengeListDTO> response = challengeService.searchChallenge(hashtag, keyword, sortType, userJobs);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PostMapping("/join/{id}")
    @Operation(summary = "챌린지 방 가입", description = "챌린지방 가입 시 사용하는 API 입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404", description = "해당 챌린지방을 찾을 수 없습니다."),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409", description = "참여인원이 가득 찼습니다."),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409", description = "이미 참여한 챌린지 입니다."),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401", description = "로그인이 필요합니다.")
    })
    public ResponseEntity<ApiResponse<Void>> joinChallenge(
            @PathVariable Long id
    ) {
        challengeService.joinChallenge(id);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @GetMapping("/myChallenges")
    @Operation(summary = "내 챌린지방 조회", description = "내가 가입되어 있는 챌린지방 조회")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401", description = "로그인이 필요합니다.")
    })
    public ResponseEntity<ApiResponse<List<ChallengeListDTO>>> myChallenges() {
        List<ChallengeListDTO> result = challengeService.myChallengeList();
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @GetMapping("/likes")
    @Operation(summary = "좋아요한 챌린지 전체조회", description = "내가 좋아요한 챌린지 전체 조회 시 사용하는 API 입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401", description = "로그인이 필요합니다.")
    })
    public ResponseEntity<ApiResponse<List<ChallengeListDTO>>> getMyLikeChallenge() {
        List<ChallengeListDTO> result = challengeService.getMyLikeChallengeList();
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

}

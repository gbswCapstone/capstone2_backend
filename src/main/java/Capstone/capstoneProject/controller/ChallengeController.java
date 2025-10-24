package Capstone.capstoneProject.controller;

import Capstone.capstoneProject.dto.*;
import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.entity.challenges.Challenges;
import Capstone.capstoneProject.service.ChallengeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/challenges")
@RequiredArgsConstructor
public class ChallengeController {


    private final ChallengeService challengeService;


    @PostMapping
    @Operation(summary = "챌린지 생성", description = "챌린지방 신규 생성 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<ChallengeDetailResponse>> createChallenge(@RequestBody ChallengeCreate dto) {
        Challenges challenges = challengeService.create(dto);
        boolean isLiked = false;
        // 엔티티를 dto로 변환
        ChallengeDetailResponse challengeDetailResponse = ChallengeDetailResponse.fromEntity(challenges, isLiked);
        return ResponseEntity.ok(ApiResponse.ok(challengeDetailResponse));
    }

    @GetMapping
    @Operation(summary = "챌린지 전체조회", description = "챌린지방 전체 조회 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<List<ChallengeListDTO>>> getChallengeList(
            @RequestParam(required = false) String sortType, @RequestParam(required = false) String job
    ) {
        List<ChallengeListDTO> result = challengeService.getChallengeList(sortType, job);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @GetMapping("/{id}")
    @Operation(summary = "챌린지 단일조회", description = "챌린지방 단일 조회 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<ChallengeDetailResponse>> getChallenge(@PathVariable Long id) {
        Challenges challenges = challengeService.findById(id);
        // 사용자가 좋아요 눌렀는 지 확인
        boolean isLiked = challengeService.isLikedByUser(challenges);
        // dto로 변환
        ChallengeDetailResponse response = ChallengeDetailResponse.fromEntity(challenges, isLiked);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PostMapping("/like/{id}")
    @Operation(summary = "챌린지방 좋아요/취소", description = "챌린지방 좋아요 or 좋아요 취소 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<ChallengeLikeResponse>> likeOrUnlike(@PathVariable Long id) {
        int updatedLikeCount = challengeService.toggleLike(id);
        return ResponseEntity.ok(ApiResponse.ok(new ChallengeLikeResponse(updatedLikeCount)));
    }

//    @PutMapping("/{id}")
//    @Operation(summary = "챌린지방 수정", description = "등록되어 있는 기존 챌린지방 수정 시 사용하는 API 입니다.")
//    public ResponseEntity<ApiResponse<ChallengeDetailResponse>> updateChallenge(
//            @PathVariable Long id,
//            @RequestBody ChallengeCreate dto) {
//        Challenges challenges = challengeService.update(id, dto);
//        boolean isLiked = challengeService.isLikedByUser(challenges);
//        ChallengeDetailResponse challengeDetailResponse = new ChallengeDetailResponse(
//                challenges, isLiked
//        );
//        return ResponseEntity.ok(ApiResponse.ok(challengeDetailResponse));
//    }

}

package Capstone.capstoneProject.controller;

import Capstone.capstoneProject.dto.ApiResponse;
import Capstone.capstoneProject.dto.ChallengeCreate;
import Capstone.capstoneProject.dto.ChallengeDetailResponse;
import Capstone.capstoneProject.entity.challenges.Challenges;
import Capstone.capstoneProject.service.ChallengeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/challenge")
@RequiredArgsConstructor
public class ChallengeController {


    private final ChallengeService challengeService;

    @PostMapping
    @Operation(summary = "게시글 생성", description = "챌린지방 신규 생성 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<ChallengeDetailResponse>> createChallenge(@RequestBody ChallengeCreate dto) {
        Challenges challenges = challengeService.create(dto);
        // 엔티티를 dto로 변환
        ChallengeDetailResponse challengeDetailResponse = ChallengeDetailResponse.fromEntity(challenges);
        return ResponseEntity.ok(ApiResponse.ok(challengeDetailResponse));

    }

}

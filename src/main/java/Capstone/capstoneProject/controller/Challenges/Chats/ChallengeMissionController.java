package Capstone.capstoneProject.controller.Challenges.Chats;

import Capstone.capstoneProject.dto.Missions.MissionCreate;
import Capstone.capstoneProject.dto.Missions.MissionResponse;
import Capstone.capstoneProject.global.ApiResponse;
import Capstone.capstoneProject.service.ChallengeMessageService;
import Capstone.capstoneProject.service.ChallengeMissionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("api/challenges/chat/missions")
@RequiredArgsConstructor
public class ChallengeMissionController {
    private final ChallengeMissionService challengeMissionService;
    private final ChallengeMessageService challengeMessageService;

    @PostMapping("/rooms/{roomId}")
    @Operation(summary = "챌린지 미션 생성 및 전송", description =
            "챌린지 미션 생성 및 전송 시 사용하는 API 입니다." + "미션 생성자는 자동으로 미션에 참가됩니다.")
    public ResponseEntity<ApiResponse<MissionResponse>> createChallengeMission
            (@PathVariable String roomId, @RequestBody MissionCreate request) {
        MissionResponse result = challengeMissionService.createChallengeMission(roomId, request);
        return ResponseEntity.ok(ApiResponse.ok(result, "생성 및 전송되었습니다."));
    }

    @PostMapping("/join/{missionId}")
    @Operation(summary = "챌린지 미션 참가하기", description = "챌린지 미션 참가 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<Void>> challengeMissionJoin(@PathVariable Long missionId) {
        challengeMissionService.joinChallengeMission(missionId);
        return ResponseEntity.ok(ApiResponse.ok("참가되었습니다."));
    }

    @DeleteMapping("/{messageId}")
    @Operation(summary = "챌린지 미션 메시지 삭제", description = "미션은 메시지 삭제와 상관업이 미션을 참가한 사람들은 계속 진행됩니다.")
    public ResponseEntity<ApiResponse<Void>> deleteChallengeMission(@PathVariable Long messageId) {
        challengeMessageService.deleteMessage(messageId);
        return ResponseEntity.ok(ApiResponse.ok("삭제되었습니다."));
    }
}

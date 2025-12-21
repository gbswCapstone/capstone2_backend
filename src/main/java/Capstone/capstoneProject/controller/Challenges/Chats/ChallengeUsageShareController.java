package Capstone.capstoneProject.controller.Challenges.Chats;

import Capstone.capstoneProject.dto.Chats.UsageShareRequest;
import Capstone.capstoneProject.global.ApiResponse;
import Capstone.capstoneProject.service.ChallengeUsageShareService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/challenges/chat")
@RequiredArgsConstructor
public class ChallengeUsageShareController {
    private final ChallengeUsageShareService challengeUsageShareService;

    @PostMapping("/rooms/{roomId}/usage-share")
    @Operation(summary = "챌린지 채팅방으로 사용내역 공유", description =
            "챌린지 채팅방으로 사용내역 공유 시 사용하는 API 입니다.\n"
            + "본인 사용내역만 공유할 수 있습니다."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403", description = "해당 채팅방 유저가 아닙니다.",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403", description = "본인 사용내역 메시지만 공유 가능합니다.",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401", description = "로그인이 필요합니다.",
                    content = @Content
            )
    })
    public ResponseEntity<ApiResponse<Void>> shareChallengeChatUsage
            (@PathVariable String roomId, @RequestBody UsageShareRequest request) {
        challengeUsageShareService.shareChallengeChatUsage(roomId, request);
        return ResponseEntity.ok(ApiResponse.ok("공유되었습니다."));
    }

    @DeleteMapping("/rooms/usage-share/{messageId}")
    @Operation(summary = "챌린지 채팅방 사용내역 삭제", description = "사용내역 삭제는 작성자와 방장만 가능합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404", description = "채팅방의 해당 메시지를 찾을 수 없습니다.",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403", description = "해당 채팅방 유저가 아닙니다.",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403", description = "채팅방의 해당 메시지에 관한 권한이 없습니다.",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "사용내역 메시지가 아닙니다.",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401", description = "로그인이 필요합니다.",
                    content = @Content
            )
    })
    public ResponseEntity<ApiResponse<Void>> deleteChallengeChatUsage
            (@PathVariable Long messageId) {
        challengeUsageShareService.deleteChallengeChatUsage(messageId);
        return ResponseEntity.ok(ApiResponse.ok("삭제되었습니다."));
    }


}

package Capstone.capstoneProject.controller.challenge;


import Capstone.capstoneProject.dto.chat.NoticeRequest;
import Capstone.capstoneProject.dto.chat.NoticeResponse;
import Capstone.capstoneProject.global.ApiResponse;
import Capstone.capstoneProject.service.challenge.ChallengeAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("api/challenge/admin")
@RequiredArgsConstructor
public class ChallengeAdminController {
    private final ChallengeAdminService challengeAdminService;

    @PostMapping("/chat/rooms/{roomId}/notices")
    @Operation(summary = "챌린지 채팅방 공지 작성", description = "공지 작성은 방장만 작성할 수 있습니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404", description = "해당 채팅방을 찾을 수 없습니다.",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403", description = "해당 채팅방 방장만 가능한 권한입니다.",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403", description = "해당 채팅방 유저가 아닙니다.",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401", description = "로그인이 필요합니다.",
                    content = @Content
            )
    })
    public ResponseEntity<ApiResponse<NoticeResponse>> createChallengeChatNotice
            (@PathVariable String roomId, @RequestBody NoticeRequest request) {
        NoticeResponse result = challengeAdminService.createNotice(roomId, request);
        return ResponseEntity.ok(ApiResponse.ok(result, "작성되었습니다."));
    }


    @PutMapping("/chat/notices/{noticeId}")
    @Operation(summary = "챌린지 채팅방 공지 수정", description = "공지 수정은 방장만 수정할 수 있습니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404", description = "해당 채팅방을 찾을 수 없습니다.",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403", description = "해당 채팅방 방장만 가능한 권한입니다.",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404", description = "채팅방 해당 공지를 찾을 수 없습니다.",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403", description = "해당 채팅방 유저가 아닙니다.",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401", description = "로그인이 필요합니다.",
                    content = @Content
            )
    })
    public ResponseEntity<ApiResponse<NoticeResponse>> patchChallengeChatNotice
            (@PathVariable Long noticeId, @RequestBody NoticeRequest request) {
        NoticeResponse result = challengeAdminService.patchNotice(noticeId, request);
        return ResponseEntity.ok(ApiResponse.ok(result, "수정되었습니다."));
    }

    @DeleteMapping("/chat/notices/{noticeId}")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404", description = "해당 채팅방을 찾을 수 없습니다.",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403", description = "해당 채팅방 방장만 가능한 권한입니다.",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404", description = "채팅방 해당 공지를 찾을 수 없습니다.",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403", description = "해당 채팅방 유저가 아닙니다.",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401", description = "로그인이 필요합니다.",
                    content = @Content
            )
    })
    @Operation(summary = "챌린지 채팅방 공지 삭제", description = "공지 삭제는 방장만 삭제할 수 있습니다.")
    public ResponseEntity<ApiResponse<Void>> deleteChallengeChatNotice
            (@PathVariable Long noticeId) {
        challengeAdminService.deleteNotice(noticeId);
        return ResponseEntity.ok(ApiResponse.ok("삭제되었습니다."));
    }
}

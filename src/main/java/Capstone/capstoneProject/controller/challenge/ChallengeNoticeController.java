package Capstone.capstoneProject.controller.challenge;

import Capstone.capstoneProject.dto.chat.NoticeDetailResponse;
import Capstone.capstoneProject.dto.chat.NoticeResponse;
import Capstone.capstoneProject.dto.chat.NoticeSummaryResponse;
import Capstone.capstoneProject.enums.DateSortType;
import Capstone.capstoneProject.global.ApiResponse;
import Capstone.capstoneProject.service.challenge.ChallengeNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("api/chat/rooms/{roomId}/notices")
@RequiredArgsConstructor
public class ChallengeNoticeController {
    private final ChallengeNoticeService challengeNoticeService;

    @GetMapping("/recent")
    @Operation(summary = "챌린지 채팅방 최신 공지 1개 조회", description = "챌린지 채팅방에 항상 떠 있는 공지입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404", description = "해당 채팅방을 찾을 수 없습니다.",
            content = @Content
    ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403", description = "해당 채팅방 유저가 아닙니다.",
            content = @Content
    ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401", description = "로그인이 필요합니다.",
            content = @Content)
    })
    public ResponseEntity<ApiResponse<NoticeSummaryResponse>> getRecentNotice(@PathVariable String roomId) {
        NoticeSummaryResponse result = challengeNoticeService.getRecentNotice(roomId);
        return ResponseEntity.ok(ApiResponse.ok(result, "조회되었습니다."));
    }

    @GetMapping
    @Operation(summary = "챌린지 채팅방 공지 전체조회", description = "챌린지 채팅방 공지 전체 조회 시 사용하는 API 입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404", description = "해당 채팅방을 찾을 수 없습니다.",
            content = @Content
    ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403", description = "해당 채팅방 유저가 아닙니다.",
            content = @Content
    ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401", description = "로그인이 필요합니다.",
            content = @Content)
    })
    public ResponseEntity<ApiResponse<List<NoticeResponse>>> getNotices
            (@PathVariable String roomId, @RequestParam(required = false) DateSortType dateSortType) {
        List<NoticeResponse> result = challengeNoticeService.getNotices(roomId, dateSortType);
        return ResponseEntity.ok(ApiResponse.ok(result, "조회되었습니다."));
    }

    @GetMapping("/{noticeId}")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404", description = "해당 채팅방을 찾을 수 없습니다.",
            content = @Content
    ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404", description = "채팅방의 해당 공지를 찾을 수 없습니다.",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403", description = "해당 채팅방 유저가 아닙니다.",
            content = @Content
    ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401", description = "로그인이 필요합니다.",
            content = @Content)
    })
    @Operation(summary = "챌린지 채팅방 공지 상세조회", description = "챌린지 채팅방에서 공지 꺽새 내릴 때 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<NoticeDetailResponse>> getNotice
            (@PathVariable String roomId, @PathVariable Long noticeId) {
        NoticeDetailResponse result = challengeNoticeService.getNotice(roomId, noticeId);
        return ResponseEntity.ok(ApiResponse.ok(result, "조회되었습니다."));
    }
}

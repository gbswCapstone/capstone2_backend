package Capstone.capstoneProject.controller.Challenges.Chats;

import Capstone.capstoneProject.dto.Chats.ChatMessageDTO;
import Capstone.capstoneProject.dto.Chats.ChatMessageSearchResponse;
import Capstone.capstoneProject.dto.Chats.MessagePatchRequest;
import Capstone.capstoneProject.dto.Chats.MessageSendRequest;
import Capstone.capstoneProject.global.ApiResponse;
import Capstone.capstoneProject.service.ChallengeMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/challenges/chat/messages")
@RequiredArgsConstructor
public class ChallengeMessageController {
    private final ChallengeMessageService challengeMessageService;


    @PostMapping("/send")
    @Operation(summary = "챌린지 채팅 메시지 전송 (WebSocket)",
            description = """
        실제 메시지 전송은 WebSocket(STOMP)을 사용합니다.
        SEND: /pub/api/challenges/chat/message
        SUBSCRIBE: /sub/challenges/chat/room/{roomId}
        """)
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404", description = "해당 채팅방을 찾을 수 없습니다.",
            content = @Content
    ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404", description = "해당 사용자를 찾을 수 없습니다.",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401", description = "로그인이 필요합니다.",
                    content = @Content
            )
    })
    public String chatSendMessage() {
        return "Swagger 표시용 엔드포인트입니다.";
    }


    @GetMapping("/rooms/{roomId}/messages")
    @Operation(summary = "챌린지 채팅방 메시지 히스토리 조회", description = "챌린지 채팅방 메시지 히스토리 조회 시 사용하는 API 입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
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
    public ResponseEntity<ApiResponse<List<ChatMessageDTO>>> getChallengeMessages
            (@PathVariable String roomId, @RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "30") int size) {
        List<ChatMessageDTO> result = challengeMessageService.getMessages(roomId, page, size);
        return ResponseEntity.ok(ApiResponse.ok(result, "조회되었습니다."));
    }

    @PutMapping("/{messageId}")
    @Operation(summary = "챌린지 채팅 메시지 수정", description = "작성자만 메시지를 수정할 수 있습니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404", description = "채팅방의 해당 메시지를 찾을 수 없습니다.",
            content = @Content
    ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403", description = "채팅방의 해당 메시지에 관한 권한이 없습니다.",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401", description = "로그인이 필요합니다.",
                    content = @Content
            )
    })
    public ResponseEntity<ApiResponse<Void>> patchMessage
            (@RequestBody MessagePatchRequest request, @PathVariable Long messageId) {
        challengeMessageService.patchMessage(request, messageId);
        return ResponseEntity.ok(ApiResponse.ok("수정되었습니다."));
    }

    @DeleteMapping("/{messageId}")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
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
                    responseCode = "401", description = "로그인이 필요합니다.",
                    content = @Content
            )
    })
    @Operation(summary = "챌린지 채팅 메시지 삭제", description = "작성자와 방장만 메시지를 삭제할 수 있습니다.")
    public ResponseEntity<ApiResponse<Void>> deleteMessage(@PathVariable Long messageId) {
        challengeMessageService.deleteMessage(messageId);
        return ResponseEntity.ok(ApiResponse.ok("삭제되었습니다."));
    }

    @GetMapping("/rooms/{roomId}/search")
    @Operation(summary = "챌린지 채팅방 메시지 검색", description = "챌린지 채팅방 메시지 검색 시 사용하는 API 입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
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
    public ResponseEntity<ApiResponse<List<ChatMessageSearchResponse>>> searchChallengeChatMessage
            (@PathVariable String roomId, @RequestParam String keyword) {
        List<ChatMessageSearchResponse> result = challengeMessageService.searchChallengeChatMessage(roomId, keyword);
        return ResponseEntity.ok(ApiResponse.ok(result, "검색되었습니다."));

    }
}

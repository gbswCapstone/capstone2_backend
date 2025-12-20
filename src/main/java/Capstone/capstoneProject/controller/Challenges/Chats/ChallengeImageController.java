package Capstone.capstoneProject.controller.Challenges.Chats;

import Capstone.capstoneProject.dto.Chats.ImageUploadRequest;
import Capstone.capstoneProject.global.ApiResponse;
import Capstone.capstoneProject.service.ChallengeImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/challenges/chat/images")
@RequiredArgsConstructor
public class ChallengeImageController {
    private final ChallengeImageService challengeImageService;

    @PostMapping("/send")
    @Operation(summary = "챌린지 채팅 이미지 전송 (WebSocket)",
            description = """
        실제 메시지 전송은 WebSocket(STOMP)을 사용합니다.
        SEND: /pub/api/challenges/chat/images
        SUBSCRIBE: /sub/challenges/chat/room/{roomId}
        """)
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ), @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404", description = "사용자를 찾을 수 없습니다.",
            content = @Content
    ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404", description = "해당 채팅방을 찾을 수 없습니다.",
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
    public String chatSendImage() {
        return "Swagger 표시용 엔드포인트입니다.";
    }

    @DeleteMapping("/{messageId}")
    @Operation(summary = "챌린지 채팅 이미지 삭제", description = "작성자와 방장만 해당 이미지를 삭제할 수 있습니다.")
    public ResponseEntity<ApiResponse<Void>> deleteImages(@PathVariable Long messageId) {
        challengeImageService.deleteImage(messageId);
        return ResponseEntity.ok(ApiResponse.ok("삭제되었습니다."));
    }

}

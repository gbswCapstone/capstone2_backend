package Capstone.capstoneProject.controller.Challenges.Chats;

import Capstone.capstoneProject.dto.Chats.ChatRoomUserResponse;
import Capstone.capstoneProject.global.ApiResponse;
import Capstone.capstoneProject.service.ChallengeChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ChallengeChatRoomController {
    private final ChallengeChatRoomService challengeChatRoomService;

    @PostMapping("/sub/challenges/chat/room/{roomId}")
    @Operation(summary = "챌린지 채팅방 구독", description = "챌린지 채팅방 구독 시 사용하는 API 입니다.")
    public String chatSubcribe() {
        return "Swagger 표시용 엔드포인트입니다.";
    }

    @GetMapping("api/chat/rooms/{roomId}/users")
    @Operation(summary = "챌린지 채팅방 유저 조회", description = "챌린지 채팅방 유저 조회 시 사용하는 API 입니다.")
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
    public ResponseEntity<ApiResponse<List<ChatRoomUserResponse>>> getChallengeChatUser
            (@PathVariable String roomId) {
        List<ChatRoomUserResponse> result = challengeChatRoomService.getChallengeChatUser(roomId);
        return ResponseEntity.ok(ApiResponse.ok(result, "조회되었습니다."));
    }

}

package Capstone.capstoneProject.controller.Challenges.Chats;

import Capstone.capstoneProject.global.ApiResponse;
import Capstone.capstoneProject.service.ChallengeImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("api/challenges/chat/images")
@RequiredArgsConstructor
public class ChallengeImageController {
    private final ChallengeImageService challengeImageService;

    @PostMapping
    @Operation(summary = "챌린지 채팅 이미지 전송",
            description = "챌린지 채팅 이미지 전송 시 사용하는 API 입니다." +
    "이미지 여러개를 보낼 시에 묶어서 보내집니다.(이미지 여러개여도 메시지 1개로 처리됨)")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
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
                        responseCode = "500", description = "firebase 이미지 업로드 실패",
                        content = @Content
                ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401", description = "로그인이 필요합니다.",
                    content = @Content
            )
    })
    public ResponseEntity<ApiResponse<Void>> uploadChatImages
            (@RequestParam("images") List<MultipartFile> images,
             @RequestParam("roomId") String roomId) {
        challengeImageService.sendImage(images, roomId);
        return ResponseEntity.ok(ApiResponse.ok("전송되었습니다."));
    }


    @DeleteMapping("/{messageId}")
    @Operation(summary = "챌린지 채팅 이미지 삭제", description = "작성자와 방장만 해당 이미지를 삭제할 수 있습니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "요청 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404", description = "해당 메시지를 찾을 수 없습니다.",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "이미지 메시지가 아닙니다.",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403", description = "채팅방의 해당 메시지에 관해 권한이 없습니다.",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403", description = "해당 채팅방 유저가 아닙니다.",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500", description = "이미지 삭제에 실패했습니다.",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401", description = "로그인이 필요합니다.",
                    content = @Content
            )
    })
    public ResponseEntity<ApiResponse<Void>> deleteImages(@PathVariable Long messageId) {
        challengeImageService.deleteImage(messageId);
        return ResponseEntity.ok(ApiResponse.ok("삭제되었습니다."));
    }

}

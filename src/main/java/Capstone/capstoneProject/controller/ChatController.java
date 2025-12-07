package Capstone.capstoneProject.controller;

import Capstone.capstoneProject.dto.Chats.ChatMessageDTO;
import Capstone.capstoneProject.dto.Chats.ChatRoomEnterResponse;
import Capstone.capstoneProject.entity.Chats.ChatRooms;
import Capstone.capstoneProject.global.ApiResponse;
import Capstone.capstoneProject.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/sub/chat/room/{roomId}")
    @Operation(summary = "챌린지 채팅방 연결", description = "챌린지 채팅방 연결 시 사용하는 API 입니다.")
    public String chatSubcribe() {
        return "Swagger 표시용 엔드포인트입니다.";
    }

    @PostMapping("/pub/chat/message")
    @Operation(summary = "챌린지 채팅방 메시지 전송", description = "챌린지 채팅방 메시지 전송 시 사용하는 API 입니다.")
    public String chatSendMessage() {
        return "Swagger 표시용 엔드포인트입니다.";
    }

    @GetMapping("api/chat/rooms/{roomId}/messages")
    @Operation(summary = "챌린지 채팅방 메시지 히스토리 조회", description = "챌린지 채팅방 메시지 히스토리 조회 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<List<ChatMessageDTO>>> getChallengeMessages
            (@PathVariable String roomId, @RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "30") int size) {
        List<ChatMessageDTO> result = chatService.getMessages(roomId, page, size);
        return ResponseEntity.ok(ApiResponse.ok(result, "조회되었습니다."));
    }






}

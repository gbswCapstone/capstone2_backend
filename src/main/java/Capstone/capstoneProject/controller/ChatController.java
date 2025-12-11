package Capstone.capstoneProject.controller;

import Capstone.capstoneProject.dto.Chats.ChatMessageDTO;
import Capstone.capstoneProject.dto.Chats.MessageSendRequest;
import Capstone.capstoneProject.global.ApiResponse;
import Capstone.capstoneProject.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/sub/chat/room/{roomId}")
    @Operation(summary = "챌린지 채팅방 구독", description = "챌린지 채팅방 구독 시 사용하는 API 입니다.")
    public String chatSubcribe() {
        return "Swagger 표시용 엔드포인트입니다.";
    }

//    @PostMapping("/pub/chat/message")
//    @Operation(summary = "챌린지 채팅방 메시지 전송", description = "챌린지 채팅방 메시지 전송 시 사용하는 API 입니다.")
//    public String chatSendMessage() {
//        return "Swagger 표시용 엔드포인트입니다.";
//    }

    @MessageMapping("api/chat/message")
    public ResponseEntity<ApiResponse<Void>> message(@Payload MessageSendRequest request) {
        chatService.sendMessage(request);
        return ResponseEntity.ok(ApiResponse.ok("전송되었습니다."));
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

package Capstone.capstoneProject.controller;

import Capstone.capstoneProject.dto.Chats.ChatRoomEnterResponse;
import Capstone.capstoneProject.entity.Chats.ChatRooms;
import Capstone.capstoneProject.global.ApiResponse;
import Capstone.capstoneProject.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/sub/chat/room/{roomId}")
    @Operation(summary = "챌린지 채팅방 구독", description = "챌린지 채팅방 구독 시 사용하는 API 입니다.")
    public String chatSubcribe() {
        return "Swagger 표시용 엔드포인트입니다.";
    }

    @PostMapping("/pub/chat/message")
    @Operation(summary = "챌린지 채팅방 메시지 전송", description = "챌린지 채팅방 메시지 전송 시 사용하는 API 입니다.")
    public String chatSendMessage() {
        return "Swagger 표시용 엔드포인트입니다.";
    }

    //    @PostMapping("/{challengeId}/chat/enter")
//    @Operation(summary = "챌린지 채팅방 입장", description = "챌린지 채팅방 입장 시 사용하는 API 입니다.")
//    public ResponseEntity<ApiResponse<ChatRoomEnterResponse>> enterChatRoom(@PathVariable Long challengeId) {
//        ChatRoomEnterResponse result = chatService.enterChatRoom(challengeId);
//        return ResponseEntity.ok(ApiResponse.ok(result, "입장하였습니다."));
//    }


}

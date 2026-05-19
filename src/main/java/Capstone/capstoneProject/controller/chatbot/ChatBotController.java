package Capstone.capstoneProject.controller.chatbot;

import Capstone.capstoneProject.dto.chatbot.ChatBotMessageDTO;
import Capstone.capstoneProject.dto.chatbot.ChatBotMessageResponse;
import Capstone.capstoneProject.global.ApiResponse;
import Capstone.capstoneProject.service.chatbot.ChatBotService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/chat/bot")
@RequiredArgsConstructor
public class ChatBotController {
    private final ChatBotService chatBotService;

    @PostMapping("/subscribe")
    @Operation(
            summary = "챗봇 채팅방 구독(WebSocket)",
            description = """
    실제 구독은 STOMP(WebSocket)를 사용합니다.

    SUBSCRIBE:
    /sub/chat/bot/{chatBotRoomId}
    """
    )
    public String chatBotSubscribe() {
        return "Swagger 표시용 엔드포인트입니다.";
    }

    @PostMapping("/send")
    @Operation(
            summary = "챗봇 메시지 전송 (WebSocket)",
            description = """
    실제 메시지 전송은 STOMP(WebSocket)를 사용합니다.

    SEND:
    /pub/api/chat/bot/messages

    Headers:
    chatBotRoomid

    SUBSCRIBE:
    /sub/chat/bot/{chatBotRoomId}
    """
    )
    public String chatBotMessage() {
        return "Swagger 표시용 엔드포인트입니다.";
    }

    @GetMapping("/chat/rooms/messages")
    @Operation(summary = "챗봇 채팅방 메시지 히스토리 조회", description = "챗봇 채팅방 메시지 히스토리 조회 시 사용하는 API 입니다.")
    public ResponseEntity<ApiResponse<List<ChatBotMessageDTO>>> getChatBotMessages
            (@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "30") int size) {
        List<ChatBotMessageDTO> result = chatBotService.getChatBotMessages(page, size);
        return ResponseEntity.ok(ApiResponse.ok(result, "조회되었습니다."));
    }

    @PostMapping("/chat/rooms/analysis/month")
    @Operation(summary = "챗봇 채팅방 이번달 소비습관 분석 메시지 생성", description = "챗봇 채팅방에서의 이번달 소비습관 분석입니다.")
    public ResponseEntity<ApiResponse<ChatBotMessageResponse>> chatRoomAnalysisMonth() {
        ChatBotMessageResponse result = chatBotService.createChatRoomAnalysisMonth();
        return ResponseEntity.ok(ApiResponse.ok(result, "생성되었습니다."));
    }

    @PostMapping("/chat/rooms/analysis/week")
    @Operation(summary = "챗봇 채팅방 이번주 소비습관 분석 메시지 생성", description = "챗봇 채팅방에서의 이번주 소비습관 분석입니다.")
    public ResponseEntity<ApiResponse<ChatBotMessageResponse>> chatRoomAnalysisWeek() {
        ChatBotMessageResponse result = chatBotService.createChatRoomAnalysisWeek();
        return ResponseEntity.ok(ApiResponse.ok(result, "생성되었습니다."));
    }


    @PostMapping("/chat/summary")
    @Operation(summary = "홈 화면 챗봇 메시지 생성", description = "유저 사용내역 기반으로 챗봇이 답변을 내려줍니다.")
    public ResponseEntity<ApiResponse<ChatBotMessageResponse>> chatSummary() {
        ChatBotMessageResponse result = chatBotService.createChatSummary();
        return ResponseEntity.ok(ApiResponse.ok(result, "생성되었습니다."));
    }







}

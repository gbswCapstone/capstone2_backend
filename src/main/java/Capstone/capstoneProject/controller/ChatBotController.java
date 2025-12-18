package Capstone.capstoneProject.controller;


import Capstone.capstoneProject.dto.ChatBot.ChatBotMessageResponse;
import Capstone.capstoneProject.dto.ChatBot.ChatRoomAnalysisResponse;
import Capstone.capstoneProject.dto.ChatBot.ChatRoomAnalysisSummary;
import Capstone.capstoneProject.global.ApiResponse;
import Capstone.capstoneProject.service.ChatBotService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/chatbot")
@RequiredArgsConstructor
public class ChatBotController {
    private final ChatBotService chatBotService;

    @PostMapping("/chat/summary")
    @Operation(summary = "홈 화면 챗봇 메시지 생성", description = "유저 사용내역 기반으로 챗봇이 답변을 내려줍니다.")
    public ResponseEntity<ApiResponse<ChatBotMessageResponse>> chatSummary() {
        ChatBotMessageResponse result = chatBotService.createChatSummary();
        return ResponseEntity.ok(ApiResponse.ok(result, "생성되었습니다."));
    }

    @PostMapping("/chat/rooms/analysis")
    @Operation(summary = "채팅방 소비습관 분석 메시지 생성", description = "챗봇 채팅방에서의 소비습관 분석입니다.")
    public ResponseEntity<ApiResponse<ChatRoomAnalysisResponse>> chatRoomAnalysis() {
        ChatRoomAnalysisResponse result = chatBotService.createChatRoomAnalysis();
        return ResponseEntity.ok(ApiResponse.ok(result, "생성되었습니다."));
    }




}

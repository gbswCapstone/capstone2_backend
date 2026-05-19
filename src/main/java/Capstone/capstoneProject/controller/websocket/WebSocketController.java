package Capstone.capstoneProject.controller.websocket;


import Capstone.capstoneProject.dto.chat.ChallengeMessageRequest;
import Capstone.capstoneProject.dto.chat.MessageSendRequest;
import Capstone.capstoneProject.service.challenge.ChallengeMessageService;
import Capstone.capstoneProject.service.chatbot.ChatBotService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private final ChallengeMessageService challengeMessageService;
    private final ChatBotService chatBotService;


    @MessageMapping("/api/challenges/chat/messages")
    @Operation(summary = "챌린지 채팅 메시지 전송", description = "챌린지 채팅 메시지 전송 시 사용하는 API 입니다.")
    public void ChallengeMessage(@Payload ChallengeMessageRequest request, SimpMessageHeaderAccessor accessor) {
        challengeMessageService.sendMessage(request, accessor);
    }

    @MessageMapping("/api/chat/bot/messages")
    @Operation(summary = "챗봇 채팅 메시지 전송", description = "챗봇 채팅 메시지 전송 시 사용하는 API 입니다.")
    public void ChatBotMessage(@Payload MessageSendRequest request, SimpMessageHeaderAccessor accessor) {
        chatBotService.sendMessage(request, accessor);
    }


}

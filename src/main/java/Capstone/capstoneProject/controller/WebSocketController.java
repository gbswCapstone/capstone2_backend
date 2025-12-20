package Capstone.capstoneProject.controller;

import Capstone.capstoneProject.dto.Chats.ImageUploadRequest;
import Capstone.capstoneProject.dto.Chats.MessageSendRequest;
import Capstone.capstoneProject.service.ChallengeImageService;
import Capstone.capstoneProject.service.ChallengeMessageService;
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
    private final ChallengeImageService challengeImageService;

    @MessageMapping("/api/challenges/chat/messages")
    @Operation(summary = "챌린지 채팅 메시지 전송", description = "챌린지 채팅 메시지 전송 시 사용하는 API 입니다.")
    public void message(@Payload MessageSendRequest request, SimpMessageHeaderAccessor accessor) {
        challengeMessageService.sendMessage(request, accessor);
    }

    @MessageMapping("/api/challenges/chat/images")
    @Operation(summary = "챌린지 채팅 이미지 전송")
    public void sendImages(@Payload ImageUploadRequest request, SimpMessageHeaderAccessor accessor) {
        challengeImageService.sendImage(request, accessor);
    }
}

package Capstone.capstoneProject.config;

import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.repository.ChatBotRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatBotRoomStompHandler implements ChannelInterceptor {

    private final ChatBotRoomRepository chatBotRoomRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
        String destination = accessor.getDestination();

        // 세션으로 유저 확인
        if (StompCommand.CONNECT.equals(command)) {
            return message;
        }

        Users sessionUser = (Users) accessor.getSessionAttributes().get("user");
        if (sessionUser == null) {
            return null;
        }

        // 챗봇 방 구독 검증
        if (StompCommand.SUBSCRIBE.equals(command)) {

            if (destination == null || !destination.contains("/chat/bot/")) {
                return null;
            }

            Long targetUserId;
            try {
                targetUserId = Long.parseLong(destination.split("/bot/")[1]);
            } catch (Exception e) {
                return null;
            }

            // 본인 챗봇 채팅방만 구독 가능
            if (!sessionUser.getId().equals(targetUserId)) {
                return null;
            }

            // 챗봇 채팅방 존재 여부 확인
            chatBotRoomRepository.findByUser(sessionUser)
                    .orElse(null);
        }

        // 메시지 전송
        if (StompCommand.SEND.equals(command)) {

            if (destination == null || !destination.contains("/chat/bot/messages")) {
                return null;
            }

            String userIdHeader = accessor.getFirstNativeHeader("userId");
            if (userIdHeader == null) {
                return null;
            }

            Long targetUserId;
            try {
                targetUserId = Long.parseLong(userIdHeader);
            } catch (Exception e) {
                return null;
            }

            // 본인 챗봇 채팅방에만 메시지 전송 가능
            if (!sessionUser.getId().equals(targetUserId)) {
                return null;
            }

            chatBotRoomRepository.findByUser(sessionUser)
                    .orElse(null);
        }

        return message;
    }
}

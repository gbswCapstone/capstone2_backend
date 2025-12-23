package Capstone.capstoneProject.config;

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

//        // CONNECT 절대 건드리지 않음
//        if (command == StompCommand.CONNECT) {
//            return message;
//        }
//
//        String destination = accessor.getDestination();
//
//        // 챗봇 경로가 아니면 그냥 통과
//        if (destination == null || !destination.contains("/chat/bot")) {
//            return message;
//        }
//
//        Users sessionUser = (Users) accessor.getSessionAttributes().get("user");
//        if (sessionUser == null) {
//            throw new ChatBotRoomAccessDeniedException("인증되지 않은 사용자");
//        }
//
//        // 구독 검증
//        if (command == StompCommand.SUBSCRIBE) {
//
//            Long targetUserId;
//            try {
//                targetUserId = Long.parseLong(destination.split("/bot/")[1]);
//            } catch (Exception e) {
//                throw new IllegalArgumentException("잘못된 구독 경로");
//            }
//
//            if (!sessionUser.getId().equals(targetUserId)) {
//                throw new IllegalStateException("본인 챗봇 방만 구독 가능");
//            }
//
//            chatBotRoomRepository.findByUser(sessionUser)
//                    .orElseThrow(() -> new IllegalStateException("챗봇 채팅방 없음"));
//        }
//
//        // 메시지 전송 검증
//        if (command == StompCommand.SEND) {
//            // 경로 검증
//            if (!destination.contains("/chat/bot/messages")) {
//                return message;
//            }
//
//            chatBotRoomRepository.findByUser(sessionUser)
//                    .orElseThrow(() -> new ChatBotRoomNotFoundException("해당 챗봇 채팅방을 찾을 수 없습니다."));
//        }
        return message;
    }
}


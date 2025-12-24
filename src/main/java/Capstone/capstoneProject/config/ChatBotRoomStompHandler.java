package Capstone.capstoneProject.config;

import Capstone.capstoneProject.entity.ChatBot.ChatBotRooms;
import Capstone.capstoneProject.entity.Users.Users;
import Capstone.capstoneProject.exceptions.notfound.ChatBotRoomNotFoundException;
import Capstone.capstoneProject.repository.ChatBotRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

//@Component
//@RequiredArgsConstructor
//public class ChatBotRoomStompHandler implements ChannelInterceptor {
//
//    private final ChatBotRoomRepository chatBotRoomRepository;
//
//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//        String destination = accessor.getDestination();
//        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//            return message;
//        }
//
//        // 채팅방 구독 시 검증
//        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
//
//            // 헤더 우선 확인
//            String chatBotRoomId = accessor.getFirstNativeHeader("chatBotRoomId");
//            Users user = (Users) accessor.getSessionAttributes().get("user");
//            if (chatBotRoomId == null) {
//                chatBotRoomId = extractRoomId(destination);
//            }
//            if (chatBotRoomId == null || chatBotRoomId.isEmpty() || chatBotRoomId.equals("messages")) {
//                throw new ChatBotRoomNotFoundException("챗봇 방 ID를 식별할 수 없습니다.");
//            }
//
//            ChatBotRooms chatBotRooms = chatBotRoomRepository.findByChatBotRoomId(chatBotRoomId)
//                    .orElseThrow(() -> new ChatBotRoomNotFoundException("챗봇 채팅방을 찾을 수 없습니다."));
//
//        }
//
//        // 메시지 전송 시 검증
//        if (StompCommand.SEND.equals(accessor.getCommand())) {
//            String chatBotRoomId = accessor.getFirstNativeHeader("chatBotRoomId");
//            // 전송 시에도 헤더가 없으면 URL에서 추출 시도 (유연성 확보)
//            if (chatBotRoomId == null) {
//                chatBotRoomId = extractRoomId(destination);
//            }
//            if (chatBotRoomId == null) {
//                throw new ChatBotRoomNotFoundException("chatBotRoomId 헤더가 누락되었습니다.");
//            }
//            ChatBotRooms chatBotRooms = chatBotRoomRepository.findByChatBotRoomId(chatBotRoomId)
//                    .orElseThrow(() -> new ChatBotRoomNotFoundException("챗봇 채팅방을 찾을 수 없습니다."));
//        }
//        return message;
//    }
//
//    private String extractRoomId(String destination) {
//        if (destination == null) return null;
//
//        String afterBot = destination.split("/bot/")[1];
//        if (afterBot.contains("/")) {
//            String[] parts = afterBot.split("/");
//            return parts[parts.length - 1];
//        }
//        return afterBot;
//    }
//
//}
@Component
@RequiredArgsConstructor
public class ChatBotRoomStompHandler implements ChannelInterceptor {

    private final ChatBotRoomRepository chatBotRoomRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String destination = accessor.getDestination();
        StompCommand command = accessor.getCommand();

        // 1. [필수] 챗봇 관련 주소(/bot/)가 포함되지 않은 모든 요청은 즉시 통과
        // 이 로직이 없으면 챌린지 구독/전송 시 여기서 에러나서 소켓이 끊깁니다.
        if (destination != null && !destination.contains("/bot/")) {
            return message;
        }

        // 2. CONNECT는 이미 JwtInterceptor에서 처리하므로 통과
        if (StompCommand.CONNECT.equals(command)) {
            return message;
        }

        // 3. 채팅방 구독(SUBSCRIBE) 시 검증
        if (StompCommand.SUBSCRIBE.equals(command)) {
            String chatBotRoomId = accessor.getFirstNativeHeader("chatBotRoomId");
            if (chatBotRoomId == null) {
                chatBotRoomId = extractRoomId(destination);
            }

            validateChatBotRoom(chatBotRoomId);
        }

        // 4. 메시지 전송(SEND) 시 검증
        if (StompCommand.SEND.equals(command)) {
            String chatBotRoomId = accessor.getFirstNativeHeader("chatBotRoomId");
            if (chatBotRoomId == null) {
                chatBotRoomId = extractRoomId(destination);
            }

            validateChatBotRoom(chatBotRoomId);
        }

        return message;
    }

    private void validateChatBotRoom(String chatBotRoomId) {
        if (chatBotRoomId == null || chatBotRoomId.isEmpty() || chatBotRoomId.equals("messages")) {
            throw new ChatBotRoomNotFoundException("챗봇 방 ID를 식별할 수 없습니다.");
        }

        chatBotRoomRepository.findByChatBotRoomId(chatBotRoomId)
                .orElseThrow(() -> new ChatBotRoomNotFoundException("챗봇 채팅방을 찾을 수 없습니다."));
    }

    private String extractRoomId(String destination) {
        if (destination == null || !destination.contains("/bot/")) return null;

        try {
            // 예: /sub/chat/bot/UUID -> UUID 추출
            // 예: /pub/api/chat/bot/messages/UUID -> UUID 추출
            String afterBot = destination.split("/bot/")[1];
            if (afterBot.contains("/")) {
                String[] parts = afterBot.split("/");
                return parts[parts.length - 1];
            }
            return afterBot;
        } catch (Exception e) {
            return null;
        }
    }
}

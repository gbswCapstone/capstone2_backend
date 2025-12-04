package Capstone.capstoneProject.config;

import Capstone.capstoneProject.dto.Chats.ChatMessageDTO;
import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.exceptions.ChatRoomAccessDeniedException;
import Capstone.capstoneProject.repository.ChatRoomUsersRepository;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final AuthenticatedUserUtils authenticatedUserUtils;
    private final ChatRoomUsersRepository chatRoomUsersRepository;
    private final SimpMessagingTemplate messagingTemplate;


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // 방 입장
        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {

            String destination = accessor.getDestination();
            Long roomId = extractRoomId(destination);

            Users user = authenticatedUserUtils.getCurrentUser();

            // 유저가 해당 채팅방 멤버인지 검증
            chatRoomUsersRepository.findByChatRoomsIdAndUsersId(roomId, user.getId())
                    .orElseThrow(() -> new ChatRoomAccessDeniedException("방 참가자가 아닙니다."));

            // 입장 메시지 전송
            ChatMessageDTO enterMessage =
                    ChatMessageDTO.enter(roomId, user.getProfile().getNickname());

            messagingTemplate.convertAndSend("/sub/chat/room/" + roomId, enterMessage);
        }

        // 방 나감
        if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {

            Users user = (Users) accessor.getSessionAttributes().get("user");
            Long roomId = (Long) accessor.getSessionAttributes().get("roomId");
            if (user != null && roomId != null) {
                ChatMessageDTO leaveMessage =
                        ChatMessageDTO.leave(roomId, user);

                messagingTemplate.convertAndSend("/sub/chat/room/" + roomId, leaveMessage);
            }
        }

        return message;
    }

    private Long extractRoomId(String destination) {
        return Long.parseLong(destination.split("/room/")[1]);
        // 만약 /sub/chat/room/10 -> 10 추출
    }

}

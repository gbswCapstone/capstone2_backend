package Capstone.capstoneProject.config;


import Capstone.capstoneProject.entity.chat.ChatRooms;
import Capstone.capstoneProject.entity.user.Users;
import Capstone.capstoneProject.exceptions.forbidden.ChatRoomAccessDeniedException;
import Capstone.capstoneProject.exceptions.notfound.ChatRoomNotFoundException;
import Capstone.capstoneProject.repository.chat.ChatRoomUsersRepository;
import Capstone.capstoneProject.repository.chat.ChatRoomsRepository;
import Capstone.capstoneProject.security.AuthenticatedUserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChallengeRoomStompHandler implements ChannelInterceptor {

    private final ChatRoomUsersRepository chatRoomUsersRepository;
    private final ChatRoomsRepository chatRoomsRepository;
    private final AuthenticatedUserUtils authenticatedUserUtils;


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String destination = accessor.getDestination();

        // 챌린지 주소(/room/)가 아니면 챗봇 메시지일 수 있으므로 바로 통과!
        if (destination != null && !destination.contains("/room/")) {
            return message;
        }


        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            return message;
        }

        // 채팅방 구독 시 검증
        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {

            String roomId = extractRoomId(destination);
            Users user = (Users) accessor.getSessionAttributes().get("user");

            ChatRooms chatRoom = chatRoomsRepository.findByRoomId(roomId)
                    .orElseThrow(() -> new ChatRoomNotFoundException("채팅방을 찾을 수 없습니다."));

            // 삭제 여부 검증
            if (chatRoom.getChallenge().getDeletedAt() != null && chatRoom.getDeletedAt() != null) {
                throw new ChatRoomNotFoundException("채팅방을 찾을 수 없습니다.");
            }

            boolean isMember = chatRoomUsersRepository.existsByChatRooms_RoomIdAndUsers(roomId, user);

            if (!isMember) {
                throw new ChatRoomAccessDeniedException("채팅방 참가자가 아닙니다.");
            }
        }

        // 메시지 전송 시 검증
        if (StompCommand.SEND.equals(accessor.getCommand())) {
            String roomId = accessor.getFirstNativeHeader("roomId");
            Users user = (Users) accessor.getSessionAttributes().get("user");

            ChatRooms chatRoom = chatRoomsRepository.findByRoomId(roomId)
                    .orElseThrow(() -> new ChatRoomNotFoundException("채팅방을 찾을 수 없습니다."));

            // 삭제 여부 검증
            if (chatRoom.getChallenge().getDeletedAt() != null && chatRoom.getDeletedAt() != null) {
                throw new ChatRoomNotFoundException("채팅방을 찾을 수 없습니다.");
            }

            if (roomId != null) {
                boolean isMember = chatRoomUsersRepository.existsByChatRooms_RoomIdAndUsers(roomId, user);

                if (!isMember) {
                    throw new ChatRoomAccessDeniedException("채팅방 참가자가 아닙니다.");
                }
            }
        }

        return message;
    }

    private String extractRoomId(String destination) {
        return destination.split("/room/")[1];
    }

}

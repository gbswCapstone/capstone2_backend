package Capstone.capstoneProject.config;

import Capstone.capstoneProject.dto.Chats.ChatMessageDTO;
import Capstone.capstoneProject.entity.Chats.ChatMessages;
import Capstone.capstoneProject.entity.Chats.ChatRoomUsers;
import Capstone.capstoneProject.entity.Chats.ChatRooms;
import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.enums.MessageType;
import Capstone.capstoneProject.exceptions.ChatRoomAccessDeniedException;
import Capstone.capstoneProject.repository.ChatMessagesRepository;
import Capstone.capstoneProject.repository.ChatRoomUsersRepository;
import Capstone.capstoneProject.repository.ChatRoomsRepository;
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
    private final ChatMessagesRepository chatMessagesRepository;
    private final ChatRoomsRepository chatRoomsRepository;


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // 방 입장
        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {

            String destination = accessor.getDestination();
            String roomId = extractRoomId(destination);

            Users user = authenticatedUserUtils.getCurrentUser();

            // 유저가 해당 채팅방 멤버인지 검증
            ChatRoomUsers chatRoomUser = chatRoomUsersRepository
                    .findByChatRooms_RoomIdAndUsers(roomId, user)
                    .orElseThrow(() -> new ChatRoomAccessDeniedException("방 참가자가 아닙니다."));

            ChatRooms chatRoom = chatRoomUser.getChatRooms();
            // 입장 메시지 전송
            ChatMessages enterEntity = ChatMessages.builder()
                    .chatRooms(chatRoom)
                    .users(user)
                    .messageType(MessageType.ENTER)
                    .content(user.getProfile().getNickname() + "님이 입장했습니다.")
                    .build();

            enterEntity = chatMessagesRepository.save(enterEntity);


            ChatMessageDTO enterMessage = ChatMessageDTO.from(enterEntity);


            messagingTemplate.convertAndSend("/sub/chat/room/" + roomId, enterMessage);

            // 세션에 roomId 저장 (DISCONNECT에서 사용)
            accessor.getSessionAttributes().put("roomId", roomId);
            accessor.getSessionAttributes().put("user", user);
        }

        // 방 나감
        if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {

            Users user = (Users) accessor.getSessionAttributes().get("user");
            String roomId = (String) accessor.getSessionAttributes().get("roomId");
            if (user != null && roomId != null) {


                ChatRooms chatRoom = chatRoomsRepository
                        .findByRoomId(roomId)
                        .orElse(null);

                if (chatRoom != null) {

                    ChatMessages leaveEntity = ChatMessages.builder()
                            .chatRooms(chatRoom)
                            .users(user)
                            .messageType(MessageType.LEAVE)
                            .content(user.getProfile().getNickname() + "님이 퇴장했습니다.")
                            .build();

                    leaveEntity = chatMessagesRepository.save(leaveEntity);

                    ChatMessageDTO leaveMessage = ChatMessageDTO.from(leaveEntity);

                    messagingTemplate.convertAndSend("/sub/chat/room/" + roomId, leaveMessage);
                }
            }
        }

        return message;
    }


    private String extractRoomId(String destination) {
        return destination.split("/room/")[1];
    }


}

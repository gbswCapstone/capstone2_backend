package Capstone.capstoneProject.dto.Chats;

import Capstone.capstoneProject.entity.Chats.ChatMessages;
import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ChatMessageDTO {
    private Long id; // 메시지 아이디
    private String roomId;        // 채팅방 ID
    private Long senderId;      // 메시지 보낸 사용자 ID
    private String senderName;  // 닉네임
    private MessageType messageType;
    private String content;
    private LocalDateTime createdAt;
    private String profileImg;

    public static ChatMessageDTO from(ChatMessages chatMessages) {
        return ChatMessageDTO.builder()
                .id(chatMessages.getId())
                .roomId(chatMessages.getChatRooms().getRoomId())
                .senderId(chatMessages.getUsers().getId())
                .senderName(chatMessages.getUsers().getProfile().getNickname())
                .messageType(chatMessages.getMessageType())
                .content(chatMessages.getContent())
                .createdAt(chatMessages.getCreatedAt())
                .profileImg(chatMessages.getUsers().getProfile().getProfileImg())
                .build();
    }
}

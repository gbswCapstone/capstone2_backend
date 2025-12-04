package Capstone.capstoneProject.dto.Chats;

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
    private Long roomId;        // 채팅방 ID
    private Long senderId;      // 메시지 보낸 사용자 ID
    private String senderName;  // 닉네임
    private MessageType messageType;
    private String content;
    private LocalDateTime createdAt;
    private String profileImg;

    public static ChatMessageDTO enter(Long roomId, String username) {
        return ChatMessageDTO.builder()
                .roomId(roomId)
                .senderName(username)
                .messageType(MessageType.SYSTEM)
                .content(username + "님이 입장했습니다.")
                .createdAt(LocalDateTime.now())
                .build();
    }
    public static ChatMessageDTO leave(Long roomId, Users user) {
        return ChatMessageDTO.builder()
                .roomId(roomId)
                .senderId(user.getId())
                .senderName(user.getProfile().getNickname())
                .messageType(MessageType.SYSTEM)
                .content(user.getProfile().getNickname() + "님이 퇴장했습니다.")
                .createdAt(LocalDateTime.now())
                .build();
    }


}

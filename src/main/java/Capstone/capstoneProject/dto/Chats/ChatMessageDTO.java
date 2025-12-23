package Capstone.capstoneProject.dto.Chats;

import Capstone.capstoneProject.dto.Usages.UsageShareDTO;
import Capstone.capstoneProject.entity.Chats.ChatMessages;
import Capstone.capstoneProject.entity.Missions.Missions;
import Capstone.capstoneProject.entity.UsageHistory;
import Capstone.capstoneProject.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class ChatMessageDTO {
    private Long id; // 메시지 아이디
    private String roomId;        // 채팅방 ID
    private Long senderId;      // 메시지 보낸 사용자 ID
    private String senderName;  // 닉네임
    private MessageType messageType;
    private String message; // 사용내역 공유시 사용안함
    private UsageShareDTO usageShareDTO; // 사용내역 공유 시 사용함
    private List<String> imageUrls; // 이미지 전용
    private MissionShareDTO missionShareDTO; // 미션 공유시 사용함
    private LocalDateTime createdAt;
    private String profileImg;
    private boolean isDeleted; // 삭제여부

    private static ChatMessageDTOBuilder base(ChatMessages message) {
        return ChatMessageDTO.builder()
                .id(message.getId())
                .roomId(message.getChatRooms().getRoomId())
                .senderId(message.getUsers().getId())
                .senderName(message.getUsers().getProfile().getNickname())
                .messageType(message.getMessageType())
                .createdAt(message.getCreatedAt())
                .profileImg(message.getUsers().getProfile().getProfileImg())
                .isDeleted(message.getIsDeleted());
    }

    // 모든 채팅 메시지 타입 조회
    public static ChatMessageDTO from(
            ChatMessages message,
            UsageHistory usageHistory,
            List<String> imageUrls,
            Missions missions,
            int currentParticipants
    ) {
        ChatMessageDTO dto = ChatMessageDTO.base(message).build();

        if (message.getIsDeleted()) {
            dto.setMessage("삭제된 메시지입니다.");
            return dto;
        }

        return switch (message.getMessageType()) {
            case IMAGE -> dto.toBuilder()
                    .imageUrls(imageUrls)
                    .build();

            case USAGE_SHARE -> dto.toBuilder()
                    .usageShareDTO(UsageShareDTO.from(usageHistory))
                    .build();

            case MISSION -> dto.toBuilder()
                    .missionShareDTO(MissionShareDTO.from(missions, currentParticipants))
                    .build();

            default -> dto.toBuilder()
                    .message(message.getMessage())
                    .build();
        };
    }

    public static ChatMessageDTO deleted(ChatMessages message) {
        return base(message)
                .message("삭제된 메시지입니다.")
                .build();
    }

    public static ChatMessageDTO baseFrom(ChatMessages message) {
        return ChatMessageDTO.builder()
                .id(message.getId())
                .roomId(message.getChatRooms().getRoomId())
                .senderId(message.getUsers().getId())
                .senderName(message.getUsers().getProfile().getNickname())
                .messageType(message.getMessageType())
                .createdAt(message.getCreatedAt())
                .profileImg(message.getUsers().getProfile().getProfileImg())
                .isDeleted(message.getIsDeleted())
                .build();
    }



    public static ChatMessageDTO text(ChatMessages message) {
        if (message.getIsDeleted()) {
            return base(message).build();
        }

        return base(message)
                .message(message.getMessage())
                .build();
    }

    public static ChatMessageDTO image(ChatMessages message, List<String> imageUrls) {
        if (message.getIsDeleted()) {
            return base(message).build();
        }

        return base(message)
                .imageUrls(imageUrls)
                .build();
    }


    public static ChatMessageDTO usageShare(ChatMessages message, UsageHistory usageHistory) {
        if (message.getIsDeleted()) {
            return base(message).build();
        }

        return base(message)
                .usageShareDTO(UsageShareDTO.from(usageHistory))
                .build();
    }

    public static ChatMessageDTO missionShare(ChatMessages messages, Missions mission, int currentParticipants) {
        if (messages.getIsDeleted()) {
            return base(messages).build();
        }

        return base(messages)
                .missionShareDTO(MissionShareDTO.from(mission, currentParticipants))
                .build();
    }


}

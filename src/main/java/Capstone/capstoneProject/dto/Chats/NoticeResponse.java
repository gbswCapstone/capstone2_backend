package Capstone.capstoneProject.dto.Chats;

import Capstone.capstoneProject.dto.UserSummaryDTO;
import Capstone.capstoneProject.entity.Chats.ChatNotices;
import Capstone.capstoneProject.entity.Chats.ChatRoomUsers;
import Capstone.capstoneProject.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class NoticeResponse {
    private boolean hasNotice; // 공지 존재 여부
    private Long id; // 공지 아이디
    private String content;
    private ChatRoomUserResponse chatRoomUserResponse;
    private LocalDateTime createdAt;

    public static NoticeResponse from(ChatNotices chatNotices, ChatRoomUsers chatRoomUsers, Users user) {
        if (chatNotices == null) {
            return new NoticeResponse(false, null, null, null, null);
        }
        return NoticeResponse.builder()
                .hasNotice(true)
                .id(chatNotices.getId())
                .content(chatNotices.getContent())
                .chatRoomUserResponse(ChatRoomUserResponse.from(chatRoomUsers, user))
                .createdAt(chatNotices.getUpdatedAt())
                .build();
    }


}

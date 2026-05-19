package Capstone.capstoneProject.dto.chat;

import Capstone.capstoneProject.entity.chat.ChatNotices;
import Capstone.capstoneProject.entity.chat.ChatRoomUsers;
import Capstone.capstoneProject.entity.user.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class NoticeDetailResponse {
    private Long id; // 공지 아이디
    private String content;
    private ChatRoomUserResponse author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static NoticeDetailResponse from(ChatNotices chatNotices, ChatRoomUsers chatRoomUsers, Users user) {
        return NoticeDetailResponse.builder()
                .id(chatNotices.getId())
                .content(chatNotices.getContent())
                .author(ChatRoomUserResponse.from(chatRoomUsers, user))
                .createdAt(chatNotices.getCreatedAt())
                .updatedAt(chatNotices.getUpdatedAt())
                .build();
    }
}

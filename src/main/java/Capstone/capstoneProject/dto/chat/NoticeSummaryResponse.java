package Capstone.capstoneProject.dto.chat;

import Capstone.capstoneProject.entity.chat.ChatNotices;
import Capstone.capstoneProject.entity.user.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class NoticeSummaryResponse {
    private boolean hasNotice; // 공지 존재 여부
    private Long id; // 공지 아이디
    private String content;
    private String nickname; // 적은사람 이름

    public static NoticeSummaryResponse from(ChatNotices chatNotices, Users user) {
        if (chatNotices == null) {
            return new NoticeSummaryResponse(false, null, null, null);
        }
        return NoticeSummaryResponse.builder()
                .hasNotice(true)
                .id(chatNotices.getId())
                .content(chatNotices.getContent())
                .nickname(user.getProfile().getNickname())
                .build();
    }
}

package Capstone.capstoneProject.dto.Chats;


import Capstone.capstoneProject.entity.Chats.ChatRoomUsers;
import Capstone.capstoneProject.entity.Users.Users;
import Capstone.capstoneProject.enums.ChatRoomRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ChatRoomUserResponse {
    private Long userId;
    private String nickname;
    private String profileImg;
    private boolean isHost; // 방장인지 조회
    private boolean isMine; // 내 프로필인지 조회

    public static ChatRoomUserResponse from(ChatRoomUsers chatRoomUsers, Users user) {
        return ChatRoomUserResponse.builder()
                .userId(chatRoomUsers.getUsers().getId())
                .nickname(chatRoomUsers.getUsers().getProfile().getNickname())
                .profileImg(chatRoomUsers.getUsers().getProfile().getProfileImg())
                .isHost(chatRoomUsers.getChatRoomRole() == ChatRoomRole.HOST)
                .isMine(chatRoomUsers.getUsers().getId().equals(user.getId()))
                .build();
    }
}

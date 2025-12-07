package Capstone.capstoneProject.dto;

import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.entity.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;

// 프로필 보기
@Getter
@AllArgsConstructor
public class UserResponseDTO {

        //유저테이블
        private final Long id;
        private final String email;
        // 유저프로필 테이블
        private final String nickname;
        private final String profileImg;
        private final String statusMessage;

    public UserResponseDTO(Users user, UserProfile profile) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = profile.getNickname();
        this.profileImg = profile.getProfileImg();
        this.statusMessage = profile.getStatusMessage();
    }

}

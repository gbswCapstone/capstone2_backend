package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.ChatBot.HomeChatBotMessages;
import Capstone.capstoneProject.entity.Users.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HomeChatBotMessageRepository extends JpaRepository<HomeChatBotMessages, Long> {
    // 유저의 최근 메시지 2건 조회
    List<HomeChatBotMessages> findTop2ByUserOrderByCreatedAtDesc(Users user);

}

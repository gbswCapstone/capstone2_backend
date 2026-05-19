package Capstone.capstoneProject.repository.chatbot;

import Capstone.capstoneProject.entity.chatbot.HomeChatBotMessages;
import Capstone.capstoneProject.entity.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HomeChatBotMessageRepository extends JpaRepository<HomeChatBotMessages, Long> {
    // 유저의 최근 메시지 2건 조회
    List<HomeChatBotMessages> findTop2ByUserOrderByCreatedAtDesc(Users user);

}

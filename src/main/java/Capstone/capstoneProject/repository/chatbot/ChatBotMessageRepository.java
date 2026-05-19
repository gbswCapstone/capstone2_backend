package Capstone.capstoneProject.repository.chatbot;


import Capstone.capstoneProject.entity.chatbot.ChatBotMessages;
import Capstone.capstoneProject.entity.chatbot.ChatBotRooms;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ChatBotMessageRepository extends JpaRepository<ChatBotMessages, Long> {
    List<ChatBotMessages> findByChatBotRooms(ChatBotRooms chatBotRooms);
    Page<ChatBotMessages> findByChatBotRooms(
            ChatBotRooms chatBotRooms,
            Pageable pageable
    );
    List<ChatBotMessages> findTop2ByChatBotRoomsOrderByCreatedAtDesc(ChatBotRooms chatBotRooms);

}

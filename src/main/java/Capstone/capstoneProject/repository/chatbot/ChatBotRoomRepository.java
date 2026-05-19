package Capstone.capstoneProject.repository.chatbot;

import Capstone.capstoneProject.entity.chatbot.ChatBotRooms;
import Capstone.capstoneProject.entity.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatBotRoomRepository extends JpaRepository<ChatBotRooms, Long> {
    Optional<ChatBotRooms> findByUser(Users user);
    Optional<ChatBotRooms> findByUserId(Long userId);
    boolean existsByUser(Users user);
    Optional<ChatBotRooms> findByChatBotRoomId(String chatBotRoomId);
}

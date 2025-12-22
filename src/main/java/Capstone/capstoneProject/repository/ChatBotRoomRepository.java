package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.ChatBot.ChatBotRooms;
import Capstone.capstoneProject.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatBotRoomRepository extends JpaRepository<ChatBotRooms, Long> {
    Optional<ChatBotRooms> findByUser(Users user);
    Optional<ChatBotRooms> findByUserId(Long userId);
    boolean existsByUser(Users user);
}

package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.Chats.ChatImages;
import Capstone.capstoneProject.entity.Chats.ChatMessages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatImagesRepository extends JpaRepository<ChatImages, Long> {
    List<String> findImageUrlByChatMessages(ChatMessages chatMessages);
    List<ChatImages> findByChatMessages(ChatMessages chatMessages);
}

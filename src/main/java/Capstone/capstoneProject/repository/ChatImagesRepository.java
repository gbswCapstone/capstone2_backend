package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.Chats.ChatImages;
import Capstone.capstoneProject.entity.Chats.ChatMessages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatImagesRepository extends JpaRepository<ChatImages, Long> {
    @Query("""
    select ci.imageUrl
    from ChatImages ci
    where ci.chatMessages = :chatMessage
""")
    List<String> findImageUrlByChatMessages(
            @Param("chatMessage") ChatMessages chatMessage
    );
    List<ChatImages> findByChatMessages(ChatMessages chatMessages);
}

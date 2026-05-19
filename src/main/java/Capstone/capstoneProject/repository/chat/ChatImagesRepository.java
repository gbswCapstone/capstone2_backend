package Capstone.capstoneProject.repository.chat;

import Capstone.capstoneProject.entity.chat.ChatImages;
import Capstone.capstoneProject.entity.chat.ChatMessages;
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

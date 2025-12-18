package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.Chats.ChatMessages;
import Capstone.capstoneProject.entity.Chats.ChatRooms;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ChatMessagesRepository extends JpaRepository<ChatMessages, Long> {
    Page<ChatMessages> findByChatRooms(ChatRooms chatRooms, Pageable pageable);


    Optional<ChatMessages> findByIdAndIsDeletedFalse(Long id);
    Page<ChatMessages> findByChatRoomsAndCreatedAtGreaterThanEqual(
            ChatRooms chatRooms,
            LocalDateTime createdAt,
            Pageable pageable
    );

    // 텍스트 메시지만 검색
    @Query("""
    SELECT m FROM ChatMessages m
    WHERE m.chatRooms.roomId = :roomId
      AND m.messageType = 'TEXT'
      AND m.content LIKE %:keyword%
    ORDER BY m.createdAt DESC
""")
    List<ChatMessages> searchByRoomIdAndKeyword(
            @Param("roomId") String roomId,
            @Param("keyword") String keyword
    );
}

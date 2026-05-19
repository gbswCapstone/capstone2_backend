package Capstone.capstoneProject.repository.chat;

import Capstone.capstoneProject.entity.chat.ChatMessages;
import Capstone.capstoneProject.entity.chat.ChatRooms;
import Capstone.capstoneProject.entity.mission.Missions;
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

    Optional<ChatMessages> findByMissionsAndIsDeletedFalse(Missions missions);

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
      AND m.message LIKE %:keyword%
    ORDER BY m.createdAt DESC
""")
    List<ChatMessages> searchByRoomIdAndKeyword(
            @Param("roomId") String roomId,
            @Param("keyword") String keyword
    );
}

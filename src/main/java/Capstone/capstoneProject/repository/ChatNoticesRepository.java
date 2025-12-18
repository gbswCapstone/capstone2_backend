package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.Chats.ChatNotices;
import Capstone.capstoneProject.entity.Chats.ChatRooms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatNoticesRepository extends JpaRepository<ChatNotices, Long> {

    // 최신순 공지 조회
    List<ChatNotices> findAllByChatRoomsOrderByCreatedAtDesc(ChatRooms chatRooms);
    List<ChatNotices> findAllByChatRoomsOrderByCreatedAtAsc(ChatRooms chatRooms);


    // 제일 최신 공지 1개 조회
    Optional<ChatNotices> findTopByChatRoomsOrderByCreatedAtDesc(ChatRooms chatRoom);
}

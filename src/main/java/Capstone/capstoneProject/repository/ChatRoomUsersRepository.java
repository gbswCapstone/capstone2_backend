package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.Chats.ChatRoomUsers;
import Capstone.capstoneProject.entity.Chats.ChatRooms;
import Capstone.capstoneProject.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomUsersRepository extends JpaRepository<ChatRoomUsers, Long> {

    Optional<ChatRoomUsers> findByChatRooms_RoomIdAndUsers(String roomId, Users users);
    boolean existsByChatRooms_RoomIdAndUsers(String roomId, Users user);
    Optional<ChatRoomUsers> findByChatRoomsAndUsers(ChatRooms chatRoom, Users user);
    List<ChatRoomUsers> findByChatRooms_RoomIdAndUsersNotOrderByCreatedAtAsc(String roomId, Users user);
    List<ChatRoomUsers> findByChatRooms_RoomIdOrderByCreatedAtAsc(String roomId);

}

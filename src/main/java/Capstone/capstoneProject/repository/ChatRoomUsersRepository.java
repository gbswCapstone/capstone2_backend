package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.Chats.ChatRoomUsers;
import Capstone.capstoneProject.entity.Chats.ChatRooms;
import Capstone.capstoneProject.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomUsersRepository extends JpaRepository<ChatRoomUsers, Long> {

    Optional<ChatRoomUsers> findByChatRoomsIdAndUsersId(Long chatLoomId, Long userId);

    Optional<ChatRoomUsers> findByChatRoomsAndUsers(ChatRooms chatRoom, Users user);


}

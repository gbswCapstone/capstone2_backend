package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.Chats.ChatRoomUsers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomUsersRepository extends JpaRepository<ChatRoomUsers, Long> {
}

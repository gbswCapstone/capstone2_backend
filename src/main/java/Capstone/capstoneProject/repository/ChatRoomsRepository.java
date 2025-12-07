package Capstone.capstoneProject.repository;


import Capstone.capstoneProject.entity.Chats.ChatRooms;
import Capstone.capstoneProject.entity.challenges.Challenges;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomsRepository extends JpaRepository<ChatRooms, Long> {
    Optional<ChatRooms> findByChallenge(Challenges challenges);
    Optional<ChatRooms> findByRoomId(String roomId);
}

package Capstone.capstoneProject.repository.chat;


import Capstone.capstoneProject.entity.chat.ChatRooms;
import Capstone.capstoneProject.entity.challenge.Challenges;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomsRepository extends JpaRepository<ChatRooms, Long> {
    Optional<ChatRooms> findByChallenge(Challenges challenges);
    Optional<ChatRooms> findByRoomId(String roomId);

    // N+1 방지: 여러 챌린지의 채팅방을 한 번에 조회
    @Query("SELECT cr FROM ChatRooms cr WHERE cr.challenge.id IN :challengeIds AND cr.deletedAt IS NULL")
    List<ChatRooms> findByChallengeIds(@Param("challengeIds") List<Long> challengeIds);
}

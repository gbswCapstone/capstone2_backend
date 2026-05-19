package Capstone.capstoneProject.repository.board;

import Capstone.capstoneProject.entity.board.BoardVideos;
import Capstone.capstoneProject.entity.board.Boards;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardVideoRepository extends JpaRepository<BoardVideos, Long> {
    void deleteByBoards(Boards boards);
}

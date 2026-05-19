package Capstone.capstoneProject.repository.board;

import Capstone.capstoneProject.entity.board.BoardImages;
import Capstone.capstoneProject.entity.board.Boards;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardImageRepository extends JpaRepository<BoardImages, Long> {
    void deleteByBoards(Boards boards);
}

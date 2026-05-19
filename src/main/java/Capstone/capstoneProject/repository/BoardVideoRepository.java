package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.Boards.BoardVideos;
import Capstone.capstoneProject.entity.Boards.Boards;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardVideoRepository extends JpaRepository<BoardVideos, Long> {
    void deleteByBoards(Boards boards);
}

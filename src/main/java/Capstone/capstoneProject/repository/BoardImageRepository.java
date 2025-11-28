package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.Boards.BoardImages;
import Capstone.capstoneProject.entity.Boards.Boards;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardImageRepository extends JpaRepository<BoardImages, Long> {
    void deleteByBoards(Boards boards);
}

package Capstone.capstoneProject.repository.experience;

import Capstone.capstoneProject.entity.experience.ExperienceHistory;
import Capstone.capstoneProject.enums.SourceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExperienceHistoryRepository extends JpaRepository<ExperienceHistory, Long> {

    boolean existsByUser_IdAndSourceTypeAndSourceId(
            Long userId,
            SourceType sourceType,
            Long sourceId
    );
}

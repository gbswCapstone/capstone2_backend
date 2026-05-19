package Capstone.capstoneProject.repository.challenge;

import Capstone.capstoneProject.entity.challenge.Challenges;
import Capstone.capstoneProject.entity.challenge.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    List<Hashtag> findByNameIn(List<String> names);
    Optional<Hashtag> findByName(String name);
}

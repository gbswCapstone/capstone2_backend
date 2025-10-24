package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.challenges.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeLikesRepository extends JpaRepository<Likes, Long> {

}

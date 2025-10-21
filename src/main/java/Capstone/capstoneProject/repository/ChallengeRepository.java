package Capstone.capstoneProject.repository;


import Capstone.capstoneProject.entity.challenges.Challenges;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenges, Long> {
}

package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.Experiences.UserExperience;
import Capstone.capstoneProject.entity.Users.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserExperienceRepository extends JpaRepository<UserExperience, Long> {
    Optional<UserExperience> findByUser(Users user);
}

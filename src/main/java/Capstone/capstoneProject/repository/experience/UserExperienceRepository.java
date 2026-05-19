package Capstone.capstoneProject.repository.experience;

import Capstone.capstoneProject.entity.experience.UserExperience;
import Capstone.capstoneProject.entity.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserExperienceRepository extends JpaRepository<UserExperience, Long> {
    Optional<UserExperience> findByUser(Users user);
}

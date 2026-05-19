package Capstone.capstoneProject.repository.user;


import Capstone.capstoneProject.entity.user.UserCharacters;
import Capstone.capstoneProject.entity.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCharacterRepository extends JpaRepository<UserCharacters, Long> {
    Optional<UserCharacters> findByUser(Users user);
}

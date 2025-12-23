package Capstone.capstoneProject.repository;


import Capstone.capstoneProject.entity.Users.UserCharacters;
import Capstone.capstoneProject.entity.Users.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCharacterRepository extends JpaRepository<UserCharacters, Long> {
    Optional<UserCharacters> findByUser(Users user);
}

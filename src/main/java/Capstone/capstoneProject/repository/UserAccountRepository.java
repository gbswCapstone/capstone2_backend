package Capstone.capstoneProject.repository;


import Capstone.capstoneProject.entity.Users.UserAccounts;
import Capstone.capstoneProject.entity.Users.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccounts, Long> {
    Optional<UserAccounts> findByUser(Users user);
}

package Capstone.capstoneProject.repository.user;


import Capstone.capstoneProject.entity.user.UserAccounts;
import Capstone.capstoneProject.entity.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccounts, Long> {
    Optional<UserAccounts> findByUser(Users user);
    boolean existsByUser(Users user);
}

package Capstone.capstoneProject.repository;

import Capstone.capstoneProject.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
    Optional<Users> findById(Long id);
    boolean existsByEmail (String email);

}

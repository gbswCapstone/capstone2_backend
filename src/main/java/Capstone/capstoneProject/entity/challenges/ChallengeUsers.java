package Capstone.capstoneProject.entity.challenges;

import Capstone.capstoneProject.entity.Users;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="challenge_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeUsers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="challenge_id", nullable = false)
    private Challenges challenge;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private Users user;

    @Column(name="joined_at", nullable = false)
    private LocalDateTime joinedAt; // 가입날짜
}

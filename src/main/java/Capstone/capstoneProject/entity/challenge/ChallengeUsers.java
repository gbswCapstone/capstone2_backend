package Capstone.capstoneProject.entity.challenge;

import Capstone.capstoneProject.entity.user.Users;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="challenge_id", nullable = false)
    private Challenges challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private Users user;

    @Column(name="joined_at", nullable = false)
    private LocalDateTime joinedAt; // 가입날짜
}

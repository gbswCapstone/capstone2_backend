package Capstone.capstoneProject.entity.challenges;

import Capstone.capstoneProject.entity.Users.Users;
import Capstone.capstoneProject.enums.UserRank;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "challenge_ranking",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_challenge_user_month",
                columnNames = {"challenge_id", "user_id", "month_year"} // unique 키 적용
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ChallengeRanking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, name="challenge_id")
    private Challenges challenge;

    @ManyToOne
    @JoinColumn(nullable = false, name="user_id")
    private Users user;

    @Column(nullable = false, name="month_year") // yyyy-mm
    private String monthYear;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name="ranking")
    private UserRank ranking;

    @CreatedDate
    @Column(nullable = false, name="created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false, name="updated_at")
    private LocalDateTime updatedAt;


}

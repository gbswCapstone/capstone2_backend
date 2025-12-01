package Capstone.capstoneProject.entity.challenges;

import Capstone.capstoneProject.entity.Users;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "challenge_user_stats",
        uniqueConstraints = @UniqueConstraint(name = "uk_challenge_user_month", columnNames = {"challenge_id", "user_id", "month_year"}))
@EntityListeners(AuditingEntityListener.class)
public class ChallengeUserStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenges challenge;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(name = "month_year", nullable = false, length = 7)
    private String monthYear; // yyyy-mm

    @Column(name = "total_spent", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalSpent;

    @Column(name = "goal_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal goalAmount;

    @Column(name = "participation_count", nullable = false)
    private int participationCount;

    @Column(name="saving_rate")
    private BigDecimal savingRate;

    @Column(name="achievement_rate")
    private BigDecimal achievementRate;

    @Column(name="challenge_xp")
    private int challengeXp;

    @Column(name = "total_days", nullable = false)
    private int totalDays;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}

package Capstone.capstoneProject.entity.Missions;

import Capstone.capstoneProject.entity.challenges.Challenges;
import Capstone.capstoneProject.enums.MissionType;
import Capstone.capstoneProject.enums.UsageCategory;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "missions")
@EntityListeners(AuditingEntityListener.class)
public class Missions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="challenge_id")
    private Challenges challenges;

    @Column(name="type", nullable = false)
    @Enumerated(EnumType.STRING)
    private MissionType missionType;

    @Column(name="title", nullable = false)
    private String title;

    @Column(name="category")
    @Enumerated(EnumType.STRING)
    private UsageCategory category;

    @Column(name="max_int")
    private int maxInt;

    @Column(name="goal_amount")
    private BigDecimal goalAmount;

    @Column(name="experience", nullable = false)
    private int experience;

    @Column(name="start_date", nullable = false)
    private LocalDate startDate;

    @Column(name="end_date", nullable = false)
    private LocalDate endDate;

    @Column(name="created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name="updated_at", nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;
}

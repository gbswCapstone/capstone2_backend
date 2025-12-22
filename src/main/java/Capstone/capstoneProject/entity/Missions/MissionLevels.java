package Capstone.capstoneProject.entity.Missions;

import Capstone.capstoneProject.enums.PeriodType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "mission_levels")
@EntityListeners(AuditingEntityListener.class)
public class MissionLevels {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="mission_id", nullable = false)
    private Missions missions;

    @Column(name="period")
    @Enumerated(EnumType.STRING)
    private PeriodType periodType;

    @Column(name="experience", nullable = false)
    private int experience;

    @Column(name="created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name="updated_at", nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;
}

package Capstone.capstoneProject.entity.Missions;

import Capstone.capstoneProject.enums.PeriodType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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

    @Column(name="period", nullable = false)
    @Enumerated(EnumType.STRING)
    private PeriodType periodType;

    @Column(name="experience", nullable = false)
    private int experience;

    @Column(name="rule", columnDefinition = "TEXT")
    private String rule;
}

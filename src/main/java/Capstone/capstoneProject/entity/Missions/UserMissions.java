package Capstone.capstoneProject.entity.Missions;

import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.enums.MissionStatusType;
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
@Table(name = "user_missions")
@EntityListeners(AuditingEntityListener.class)
public class UserMissions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private Users users;

    @ManyToOne
    @JoinColumn(name="mission_id", nullable = false)
    private Missions missions;

    @Column(name="status", nullable = false)
    @Enumerated(EnumType.STRING)
    private MissionStatusType missionStatusType;

    @Column(name="progressPercentage")
    private int progressPercentage;

    @Column(name="completed_at")
    private LocalDateTime completedAt;

    @Column(name="experience")
    private int experience;

    @Column(name="created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name="updated_at", nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;
}

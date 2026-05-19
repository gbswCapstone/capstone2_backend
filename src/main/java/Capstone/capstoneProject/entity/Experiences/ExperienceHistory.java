package Capstone.capstoneProject.entity.Experiences;

import Capstone.capstoneProject.entity.Users.Users;
import Capstone.capstoneProject.enums.SourceType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Entity
@Table(name="experience_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ExperienceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private Users user;

    @Column(name="source_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SourceType sourceType;

    @Column(name="source_id")
    private Long sourceId;

    @Column(name="experience", nullable = false)
    private int experience;

    @Column(name="description")
    private String description;

    @Column(name="created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

}

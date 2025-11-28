package Capstone.capstoneProject.entity.Boards;

import Capstone.capstoneProject.entity.Users;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name="board_likes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class BoardLikes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="board_id", nullable = false)
    private Boards boards;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private Users users;

    @Column(name="created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

}

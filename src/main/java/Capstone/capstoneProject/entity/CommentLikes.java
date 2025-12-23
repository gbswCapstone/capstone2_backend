package Capstone.capstoneProject.entity;

import Capstone.capstoneProject.entity.Users.Users;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name="comment_likes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class CommentLikes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="comment_id", nullable = false)
    private Comments comments;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private Users users;

    @Column(name="created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

}

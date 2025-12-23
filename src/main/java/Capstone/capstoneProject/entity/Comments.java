package Capstone.capstoneProject.entity;

import Capstone.capstoneProject.entity.Boards.Boards;
import Capstone.capstoneProject.entity.Users.Users;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private Users users;

    @ManyToOne
    @JoinColumn(name="board_id", nullable = false)
    private Boards boards;

    @ManyToOne
    @JoinColumn(name="parent_id")
    private Comments parentId;

    @Column(name="content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name="like_count", nullable = false)
    private int likeCount;

    @Column(name="created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name="updated_at", nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    // 관계
    @OneToMany(mappedBy = "comments", cascade = CascadeType.ALL)
    private List<CommentLikes> commentLikesList = new ArrayList<>();


}

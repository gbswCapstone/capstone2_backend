package Capstone.capstoneProject.entity.board;

import Capstone.capstoneProject.entity.comment.Comments;
import Capstone.capstoneProject.entity.user.Users;
import Capstone.capstoneProject.enums.BoardCategory;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="boards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Boards {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private Users users;

    @Column(name="category", nullable = false)
    @Enumerated(EnumType.STRING)
    private BoardCategory category;

    @Column(name="title", nullable = false)
    private String title;

    @Column(name="content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name="like_count", nullable = false)
    private int likeCount;

    @Column(name="comment_count", nullable = false)
    private int commentCount;

    @Column(name="created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name="updated_at", nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(name="deleted_at")
    private LocalDateTime deletedAt;

    // 관계
    @OneToMany(mappedBy = "boards", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Comments> commentsList = new ArrayList<>();

    @OneToMany(mappedBy = "boards", cascade = CascadeType.ALL)
    @Builder.Default
    private List<BoardImages> boardImages = new ArrayList<>();


    @OneToMany(mappedBy = "boards", cascade = CascadeType.ALL)
    @Builder.Default
    private List<BoardVideos> boardVideos = new ArrayList<>();

    @OneToMany(mappedBy = "boards", cascade = CascadeType.ALL)
    @Builder.Default
    private List<BoardLikes> boardLikes = new ArrayList<>();
}

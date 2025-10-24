package Capstone.capstoneProject.entity.challenges;

import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.enums.UserJobs;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="challenges")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Challenges {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private Users createdBy; // 방 만든 사람

    @Column(nullable = false, name="title")
    private String title;

    @Column(nullable = false, name="max_personnel") // 인원 제한
    private int maxPersonnel;

    @Column(name="image")
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserJobs job;  // 기본값 선택없음

    @Column(name="goal") // 목표
    private String goal;

    // 해시태그랑 매핑
    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ChallengeHashtag> challengeHashtags = new HashSet<>();

    @Column(nullable = false, name="like_count")
    private int likeCount;

    @Column(nullable = false, name="created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false, name="updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(name="deleted_at")
    private LocalDateTime deletedAt;

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

}

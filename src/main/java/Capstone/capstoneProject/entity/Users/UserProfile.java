package Capstone.capstoneProject.entity.Users;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;



@Entity
@Table(name = "user_profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) // 이 코드로 유저도 삭제되면 프로필도 함께삭제
    private Users user;

    @Column(nullable = false, length = 100)
    private String nickname;

    @Column(length = 1000)
    private String profileImg;

    @Column(columnDefinition = "TEXT")
    private String statusMessage;

}

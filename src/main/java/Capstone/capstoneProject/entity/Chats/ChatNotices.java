package Capstone.capstoneProject.entity.Chats;

import Capstone.capstoneProject.entity.Users;
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
@Table(name = "chat_notices")
@EntityListeners(AuditingEntityListener.class)
public class ChatNotices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="created_by", nullable = false)
    private Users users;

    @ManyToOne
    @JoinColumn(name="chat_room_id", nullable = false)
    private ChatRooms chatRooms;

    @Column(name="title", nullable = false)
    private String title;

    @Column(name="content", columnDefinition = "TEXT")
    private String content;

    @Column(name="created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name="updated_at", nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;
}

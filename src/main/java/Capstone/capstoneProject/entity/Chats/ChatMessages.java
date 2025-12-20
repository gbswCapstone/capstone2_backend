package Capstone.capstoneProject.entity.Chats;

import Capstone.capstoneProject.entity.Missions.Missions;
import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.enums.MessageType;
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
@Table(name = "chat_messages")
@EntityListeners(AuditingEntityListener.class)
public class ChatMessages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="mission_id")
    private Missions missions;

    @ManyToOne
    @JoinColumn(name="sender", nullable = false)
    private Users users;

    @ManyToOne
    @JoinColumn(name="chat_room_id", nullable = false)
    private ChatRooms chatRooms;

    @Column(name="type", nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @Column(name="content", columnDefinition = "TEXT")
    private String content;

    @Column(name="is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name="created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name="updated_at", nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(name="usage_id")
    private Long usageId;

    @PrePersist
    public void prePersist() {
        if (isDeleted == null) {
            isDeleted = false;
        }
    }
}

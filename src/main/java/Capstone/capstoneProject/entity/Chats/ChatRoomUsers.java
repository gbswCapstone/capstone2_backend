package Capstone.capstoneProject.entity.Chats;

import Capstone.capstoneProject.entity.Users;
import Capstone.capstoneProject.enums.ChatRoomRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "chat_room_users")
@EntityListeners(AuditingEntityListener.class)
public class ChatRoomUsers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="chat_room_id")
    private ChatRooms chatRooms;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Users users;

    @Column(name="created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name="deleted_at")
    private LocalDateTime deletedAt;

    @Column(name="chat_room_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private ChatRoomRole chatRoomRole;
}

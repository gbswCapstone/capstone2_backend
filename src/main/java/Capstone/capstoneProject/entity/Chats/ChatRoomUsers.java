package Capstone.capstoneProject.entity.Chats;

import Capstone.capstoneProject.entity.Users;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "chat_room_users")
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
}

package Capstone.capstoneProject.entity.Chats;

import Capstone.capstoneProject.entity.challenges.Challenges;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "chat_rooms")
public class ChatRooms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="challenge_id", nullable = false)
    private Challenges challenge;


    @Column(name="room_id", nullable = false, unique = true)
    private String roomId;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name="updated_at", nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    // 관계
    @OneToMany(mappedBy = "chatRooms")
    private List<ChatMessages> messages = new ArrayList<>();
}

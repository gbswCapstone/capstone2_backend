package Capstone.capstoneProject.entity.ChatBot;

import Capstone.capstoneProject.enums.ChatBotSenderType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name="chat_bot_messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ChatBotMessages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="chat_bot_room_id", nullable = false)
    private ChatBotRooms chatBotRooms;

    @Column(name="sender", nullable = false)
    @Enumerated(EnumType.STRING)
    private ChatBotSenderType senderType;

    @Column(name="message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name="created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;
}

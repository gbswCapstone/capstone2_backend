package Capstone.capstoneProject.entity.chatbot;

import Capstone.capstoneProject.entity.user.Users;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name="home_chat_bot_messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class HomeChatBotMessages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private Users user;

    @Column(name="message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name="created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

}

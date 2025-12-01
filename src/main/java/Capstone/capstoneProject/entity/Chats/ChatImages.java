package Capstone.capstoneProject.entity.Chats;

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
@Table(name = "chat_images")
public class ChatImages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="message_id", nullable = false)
    private ChatMessages chatMessages;

    @Column(name="image_url", nullable = false)
    private String imageUrl;

    @Column(name="created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;
}

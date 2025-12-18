package Capstone.capstoneProject.entity;


import Capstone.capstoneProject.enums.CharacterType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name="user_characters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCharacters {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private Users user;

    @Column(name="level", nullable = false)
    private int level;

    @Column(name="type", nullable = false)
    @Enumerated(EnumType.STRING)
    private CharacterType type;

    @Column(name="rating", nullable = false)
    private String rating;

    @Column(name="experience", nullable = false)
    private int experience;

    @CreatedDate
    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name="updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

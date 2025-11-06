package Capstone.capstoneProject.entity;


import Capstone.capstoneProject.enums.HistoryType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="usage_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsageHistory {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users users;

    @Column(nullable = false, name="name")
    private String name;

    @Column(nullable = false,  precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private LocalDateTime proDate;

    @Column(name="history_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private HistoryType historyType;

}

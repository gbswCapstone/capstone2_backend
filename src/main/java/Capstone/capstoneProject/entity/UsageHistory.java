package Capstone.capstoneProject.entity;


import Capstone.capstoneProject.enums.HistoryType;
import Capstone.capstoneProject.enums.UsageCategory;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    @Enumerated(EnumType.STRING)
    private UsageCategory category;

    @Column(name="pro_date")
    private LocalDate proDate;

    @Column(name="amount", nullable = false)
    private int amount;

    @Column(name="created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name="history_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private HistoryType historyType;

    public void update(String name, BigDecimal price, UsageCategory category, int amount) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.amount = amount;
    }
}

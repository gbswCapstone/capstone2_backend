package Capstone.capstoneProject.entity.user;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="user_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class UserAccounts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private Users user;

    @Column(name="balance", nullable = false, precision = 18, scale = 2)
    private BigDecimal balance;

    @Column(name="this_income", nullable = false, precision = 18, scale = 2)
    private BigDecimal thisIncome;

    @Column(name="this_outlay", nullable = false, precision = 18, scale = 2)
    private BigDecimal thisOutlay;

    @Column(name="this_month", nullable = false)
    private String thisMonth;

    @CreatedDate
    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name="updated_at", nullable = false)
    private LocalDateTime updatedAt;

}

package Backend.entity.Contract;

import Backend.Enmu.ContractStatus;
import Backend.entity.Auth.User;
import Backend.entity.Bid.Bid;
import Backend.entity.Milestone.Milestone;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "contracts")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Contract{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "bid_id", nullable = false, unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Bid bid;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User client;

    @ManyToOne
    @JoinColumn(name = "freelancer_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User freelancer;

    private LocalDate startDate;
    private LocalDate endDate;

    @Column(nullable = false)
    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContractStatus status;

    @PrePersist
    public void prePersist() {
        if (status == null) status = ContractStatus.ACTIVE;
    }

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Milestone> milestones;
}

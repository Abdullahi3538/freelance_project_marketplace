package Backend.entity.Milestone;

import Backend.Enmu.MilestoneStatus;
import Backend.entity.Contract.Contract;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "milestones")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Milestone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDate dueDate;

    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MilestoneStatus status;

    @ManyToOne
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = MilestoneStatus.PENDING;
        }
    }
}

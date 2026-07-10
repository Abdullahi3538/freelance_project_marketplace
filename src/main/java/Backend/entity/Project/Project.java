package Backend.entity.Project;

import Backend.Enmu.ProjectStatus;
import Backend.entity.Auth.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "projects")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Double budget;

    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private User client;

    @PrePersist
    public void prePersist() {

        createdAt = LocalDateTime.now();

        if (status == null) {
            status = ProjectStatus.OPEN;
        }

    }

}

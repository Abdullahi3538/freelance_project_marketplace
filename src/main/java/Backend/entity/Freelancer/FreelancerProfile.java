package Backend.entity.Freelancer;

import Backend.entity.Auth.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "freelancer_profiles")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FreelancerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @PositiveOrZero
    private Integer experienceYears;

    @PositiveOrZero
    private Double hourlyRate;

    private String portfolioUrl;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonIgnore
    private User user;
}
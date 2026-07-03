package Backend.entity.Freelancer;

import Backend.Enmu.SkillLevel;
import Backend.entity.Auth.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Entity
@Table(name = "freelancer_skills")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FreelancerSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "freelancer_id", nullable = false)
    private User freelancer;

    @ManyToOne
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SkillLevel level;

    @PositiveOrZero
    @Column(nullable = false)
    private Integer yearsOfExperience;
}
package Backend.entity.Freelancer;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "skills")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}

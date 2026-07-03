package Backend.dto.FreelancerSkillDTO;

import Backend.Enmu.SkillLevel;
import lombok.Data;

@Data
public class FreelancerSkillResponseDTO {
    private Long id;
    private String freelancerName;
    private String skillName;
    private SkillLevel level;
    private Integer yearsOfExperience;
}

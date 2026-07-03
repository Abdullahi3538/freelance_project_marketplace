package Backend.dto.FreelancerSkillDTO;

import Backend.Enmu.SkillLevel;
import lombok.Data;

@Data
public class FreelancerSkillRequestDTO {
    private Long freelancerId;
    private Long skillId;
    private SkillLevel level;
    private Integer yearsOfExperience;
}

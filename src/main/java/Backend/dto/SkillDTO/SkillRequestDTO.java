package Backend.dto.SkillDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SkillRequestDTO {
    @NotBlank(message = "Skill name is required")
    private String name;
}

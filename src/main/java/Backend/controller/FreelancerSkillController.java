package Backend.Controller;

import Backend.dto.FreelancerSkillDTO.FreelancerSkillRequestDTO;
import Backend.dto.FreelancerSkillDTO.FreelancerSkillResponseDTO;
import Backend.service.FreelancerSkillService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/freelancer-skills")

public class FreelancerSkillController {

    private final FreelancerSkillService freelancerSkillService;

    public FreelancerSkillController(FreelancerSkillService freelancerSkillService) {
        this.freelancerSkillService = freelancerSkillService;
    }

    @PostMapping
    public FreelancerSkillResponseDTO addSkill(@RequestBody FreelancerSkillRequestDTO request) {
        return freelancerSkillService.addSkill(request);
    }
}

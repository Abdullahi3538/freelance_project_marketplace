package Backend.controller;

import Backend.dto.SkillDTO.SkillRequestDTO;
import Backend.dto.SkillDTO.SkillResponseDTO;
import Backend.service.SkillService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/skills")

public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping
    public SkillResponseDTO createSkill(@RequestBody SkillRequestDTO request) {
        return skillService.createSkill(request);
    }
}
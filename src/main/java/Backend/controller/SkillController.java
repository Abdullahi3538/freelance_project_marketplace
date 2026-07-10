package Backend.controller;

import Backend.dto.SkillDTO.SkillRequestDTO;
import Backend.dto.SkillDTO.SkillResponseDTO;
import Backend.service.SkillService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")

public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping
    public SkillResponseDTO createSkill(@Valid @RequestBody SkillRequestDTO request) {
        return skillService.createSkill(request);
    }

    @GetMapping
    public List<SkillResponseDTO> getAllSkills() {
        return skillService.getAllSkills();
    }
}

package Backend.service;

import Backend.dto.SkillDTO.SkillRequestDTO;
import Backend.dto.SkillDTO.SkillResponseDTO;
import Backend.entity.Freelancer.Skill;
import Backend.repository.SkillRepository;
import org.springframework.stereotype.Service;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public SkillResponseDTO createSkill(SkillRequestDTO request) {

        if (skillRepository.existsByName(request.getName())) {
            throw new RuntimeException("Skill already exists");
        }

        Skill skill = new Skill();
        skill.setName(request.getName());

        skillRepository.save(skill);

        SkillResponseDTO response = new SkillResponseDTO();
        response.setId(skill.getId());
        response.setName(skill.getName());

        return response;
    }
}

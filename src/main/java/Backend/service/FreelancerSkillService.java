package Backend.service;

import Backend.dto.FreelancerSkillDTO.FreelancerSkillRequestDTO;
import Backend.dto.FreelancerSkillDTO.FreelancerSkillResponseDTO;
import Backend.entity.Auth.User;
import Backend.entity.Freelancer.FreelancerSkill;
import Backend.entity.Freelancer.Skill;
import Backend.repository.FreelancerSkillRepository;
import Backend.repository.SkillRepository;
import Backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FreelancerSkillService {

    private final FreelancerSkillRepository freelancerSkillRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;



    public FreelancerSkillResponseDTO addSkill(FreelancerSkillRequestDTO request) {

        User freelancer = userRepository.findById(request.getFreelancerId())
                .orElseThrow(() -> new RuntimeException("Freelancer not found"));

        Skill skill = skillRepository.findById(request.getSkillId())
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        FreelancerSkill fs = new FreelancerSkill();
        fs.setFreelancer(freelancer);
        fs.setSkill(skill);
        fs.setLevel(request.getLevel());
        fs.setYearsOfExperience(request.getYearsOfExperience());

        freelancerSkillRepository.save(fs);

        FreelancerSkillResponseDTO response = new FreelancerSkillResponseDTO();
        response.setId(fs.getId());
        response.setFreelancerName(freelancer.getFullName());
        response.setSkillName(skill.getName());
        response.setLevel(fs.getLevel());
        response.setYearsOfExperience(fs.getYearsOfExperience());

        return response;
    }
}

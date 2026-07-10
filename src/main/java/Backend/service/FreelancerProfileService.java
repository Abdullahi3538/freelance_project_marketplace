package Backend.service;



import Backend.dto.Freelancerdto.FreelancerProfileRequestDTO;
import Backend.dto.Freelancerdto.FreelancerProfileResponseDTO;
import Backend.entity.Auth.User;
import Backend.entity.Freelancer.FreelancerProfile;
import Backend.entity.Freelancer.Skill;

import Backend.repository.FreelancerProfileRepository;
import Backend.repository.SkillRepository;
import Backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FreelancerProfileService {

    @Autowired
    private final FreelancerProfileRepository profileRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final SkillRepository skillRepository;



    public FreelancerProfileResponseDTO createProfile(FreelancerProfileRequestDTO request) {

        if (profileRepository.existsByUserId(request.getUserId())) {
            throw new RuntimeException("This user already has a freelancer profile");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        FreelancerProfile profile = new FreelancerProfile();

        profile.setBio(request.getBio());
        profile.setExperienceYears(request.getExperienceYears());
        profile.setHourlyRate(request.getHourlyRate());
        profile.setPortfolioUrl(request.getPortfolioUrl());
        profile.setUser(user);
        profile.setSkills(getSkills(request.getSkillIds()));

        profileRepository.save(profile);

        return mapToResponse(profile);
    }

    public List<FreelancerProfileResponseDTO> getAllProfiles() {
        return profileRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private List<Skill> getSkills(List<Long> skillIds) {
        if (skillIds == null || skillIds.isEmpty()) {
            return List.of();
        }

        List<Skill> skills = skillRepository.findAllById(skillIds);
        if (skills.size() != skillIds.size()) {
            throw new RuntimeException("One or more skills were not found");
        }
        return skills;
    }

    private FreelancerProfileResponseDTO mapToResponse(FreelancerProfile profile) {
        User user = profile.getUser();
        FreelancerProfileResponseDTO response = new FreelancerProfileResponseDTO();

        response.setId(profile.getId());
        response.setBio(profile.getBio());
        response.setExperienceYears(profile.getExperienceYears());
        response.setHourlyRate(profile.getHourlyRate());
        response.setPortfolioUrl(profile.getPortfolioUrl());

        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setSkills(
                profile.getSkills()
                        .stream()
                        .map(Skill::getName)
                        .toList()
        );

        return response;
    }

}

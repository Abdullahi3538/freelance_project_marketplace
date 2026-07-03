package Backend.service;



import Backend.dto.Freelancerdto.FreelancerProfileRequestDTO;
import Backend.dto.Freelancerdto.FreelancerProfileResponseDTO;
import Backend.entity.Auth.User;
import Backend.entity.Freelancer.FreelancerProfile;

import Backend.repository.FreelancerProfileRepository;
import Backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FreelancerProfileService {

    @Autowired
    private final FreelancerProfileRepository profileRepository;
    @Autowired
    private final UserRepository userRepository;



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

        profileRepository.save(profile);

        FreelancerProfileResponseDTO response = new FreelancerProfileResponseDTO();

        response.setId(profile.getId());
        response.setBio(profile.getBio());
        response.setExperienceYears(profile.getExperienceYears());
        response.setHourlyRate(profile.getHourlyRate());
        response.setPortfolioUrl(profile.getPortfolioUrl());

        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());

        return response;
    }

}

package Backend.controller;



import Backend.dto.Freelancerdto.FreelancerProfileRequestDTO;
import Backend.dto.Freelancerdto.FreelancerProfileResponseDTO;
import Backend.service.FreelancerProfileService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/freelancer-profile")
@AllArgsConstructor
public class FreelancerProfileController {

    private final FreelancerProfileService profileService;

    @PostMapping
    public FreelancerProfileResponseDTO createProfile(@RequestBody FreelancerProfileRequestDTO request) {

        return profileService.createProfile(request);
    }
}
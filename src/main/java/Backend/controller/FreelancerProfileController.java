package Backend.controller;

import Backend.dto.Freelancerdto.FreelancerProfileRequestDTO;
import Backend.dto.Freelancerdto.FreelancerProfileResponseDTO;
import Backend.service.FreelancerProfileService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/freelancer-profile")
@AllArgsConstructor
public class FreelancerProfileController {

    private final FreelancerProfileService profileService;

    @PostMapping
    public FreelancerProfileResponseDTO createProfile(@Valid @RequestBody FreelancerProfileRequestDTO request) {

        return profileService.createProfile(request);
    }

    @GetMapping
    public List<FreelancerProfileResponseDTO> getAllProfiles() {
        return profileService.getAllProfiles();
    }
}

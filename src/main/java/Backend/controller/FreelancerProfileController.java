package Backend.controller;

import Backend.dto.Freelancerdto.FreelancerProfileRequestDTO;
import Backend.dto.Freelancerdto.FreelancerProfileResponseDTO;
import Backend.service.FreelancerProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/freelancer-profiles")
@RequiredArgsConstructor
public class FreelancerProfileController {

    private final FreelancerProfileService profileService;

    // FREELANCER creates their own profile (user is taken from JWT automatically)
    @PostMapping
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<FreelancerProfileResponseDTO> createProfile(
            @Valid @RequestBody FreelancerProfileRequestDTO request) {
        return new ResponseEntity<>(profileService.createProfile(request), HttpStatus.CREATED);
    }

    // Any authenticated user can browse all freelancer profiles
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<FreelancerProfileResponseDTO>> getAllProfiles() {
        return ResponseEntity.ok(profileService.getAllProfiles());
    }

    // FREELANCER gets their OWN profile (used on My Profile page)
    @GetMapping("/me")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<FreelancerProfileResponseDTO> getMyProfile() {
        return ResponseEntity.ok(profileService.getMyProfile());
    }

    // Get a specific freelancer profile by ID
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<FreelancerProfileResponseDTO> getProfileById(@PathVariable Long id) {
        return ResponseEntity.ok(profileService.getProfileById(id));
    }

    // FREELANCER updates only their own profile
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<FreelancerProfileResponseDTO> updateProfile(
            @PathVariable Long id,
            @Valid @RequestBody FreelancerProfileRequestDTO request) {
        return ResponseEntity.ok(profileService.updateProfile(id, request));
    }
}

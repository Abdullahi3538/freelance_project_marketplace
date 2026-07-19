package Backend.service;

import Backend.dto.Freelancerdto.FreelancerProfileRequestDTO;
import Backend.dto.Freelancerdto.FreelancerProfileResponseDTO;
import Backend.entity.Auth.User;
import Backend.entity.Freelancer.FreelancerProfile;
import Backend.exception.ResourceAlreadyExistsException;
import Backend.exception.ResourceNotFoundException;
import Backend.repository.FreelancerProfileRepository;
import Backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FreelancerProfileService {

    private final FreelancerProfileRepository profileRepository;
    private final UserRepository userRepository;

    // Create profile — automatically linked to the currently logged-in FREELANCER
    public FreelancerProfileResponseDTO createProfile(FreelancerProfileRequestDTO request) {
        // Extract the logged-in user's email from the SecurityContext (set by JwtFilter)
        User currentUser = getCurrentUser();

        if (profileRepository.existsByUserId(currentUser.getId())) {
            throw new ResourceAlreadyExistsException("You already have a freelancer profile");
        }

        FreelancerProfile profile = new FreelancerProfile();
        profile.setBio(request.getBio());
        profile.setExperienceYears(request.getExperienceYears());
        profile.setHourlyRate(request.getHourlyRate());
        profile.setPortfolioUrl(request.getPortfolioUrl());
        profile.setUser(currentUser);
        
        // Clean up the skills (e.g. "React " -> "react")
        profile.setSkills(cleanSkills(request.getSkills()));

        profileRepository.save(profile);

        return mapToResponse(profile);
    }

    // Get all profiles — any authenticated user
    public List<FreelancerProfileResponseDTO> getAllProfiles() {
        return profileRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Get the current logged-in freelancer's own profile
    public FreelancerProfileResponseDTO getMyProfile() {
        User currentUser = getCurrentUser();
        FreelancerProfile profile = profileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("You do not have a profile yet"));
        return mapToResponse(profile);
    }

    // Get profile by ID
    public FreelancerProfileResponseDTO getProfileById(Long id) {
        FreelancerProfile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Freelancer profile not found with id: " + id));
        return mapToResponse(profile);
    }

    // Update own profile — only the logged-in FREELANCER can update their own profile
    public FreelancerProfileResponseDTO updateProfile(Long id, FreelancerProfileRequestDTO request) {
        FreelancerProfile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Freelancer profile not found with id: " + id));

        User currentUser = getCurrentUser();

        // Make sure the profile belongs to the logged-in user
        if (!profile.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not authorized to update this profile");
        }

        profile.setBio(request.getBio());
        profile.setExperienceYears(request.getExperienceYears());
        profile.setHourlyRate(request.getHourlyRate());
        profile.setPortfolioUrl(request.getPortfolioUrl());
        profile.setSkills(cleanSkills(request.getSkills()));

        profileRepository.save(profile);

        return mapToResponse(profile);
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    // Get the currently authenticated User from the SecurityContext
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // getName() returns the email (set in JwtFilter)
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }

    // Clean and normalize skills (trim whitespace and lowercase)
    private List<String> cleanSkills(List<String> skills) {
        if (skills == null || skills.isEmpty()) {
            return List.of();
        }
        return skills.stream()
                .filter(s -> s != null && !s.trim().isEmpty())
                .map(s -> s.trim().toLowerCase())
                .distinct() // Remove duplicates like ["java", "Java"]
                .toList();
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
        
        // Return skills directly as they are already Strings
        response.setSkills(profile.getSkills());

        return response;
    }
}

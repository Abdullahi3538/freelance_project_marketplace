package Backend.service;

import Backend.entity.Auth.User;
import Backend.entity.Project.Project;
import Backend.dto.projectdto.ProjectResponseDto;
import Backend.dto.projectdto.projectRequestDTO;
import Backend.exception.ResourceNotFoundException;
import Backend.repository.ProjectRepository;
import Backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProjectService {

    private ProjectRepository projectRepository;
    private UserRepository userRepository;

    public ProjectResponseDto createProject(projectRequestDTO request) {
        // Extract the logged-in user's email from the SecurityContext
        User client = getCurrentUser();

        Project project = new Project();
        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setBudget(request.getBudget());
        project.setDeadline(request.getDeadline());
        project.setClient(client);

        projectRepository.save(project);

        return mapToResponse(project);
    }

    public List<ProjectResponseDto> getAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ProjectResponseDto> getMyProjects() {
        User client = getCurrentUser();
        return projectRepository.findByClientId(client.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Update an existing project
    public ProjectResponseDto updateProject(Long id, projectRequestDTO request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        User currentClient = getCurrentUser();

        // Ensure the logged-in client is the owner of this project
        if (!project.getClient().getId().equals(currentClient.getId())) {
            throw new RuntimeException("You are not authorized to update this project");
        }

        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setBudget(request.getBudget());
        project.setDeadline(request.getDeadline());
        
        // Note: Status changes (Open -> In Progress) usually happen in a different specialized endpoint
        // during the Bidding/Contract phase, but basic details can be updated here.

        projectRepository.save(project);
        return mapToResponse(project);
    }

    // Delete a project
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        User currentClient = getCurrentUser();

        // Ensure the logged-in client is the owner of this project
        if (!project.getClient().getId().equals(currentClient.getId())) {
            throw new RuntimeException("You are not authorized to delete this project");
        }

        projectRepository.delete(project);
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    // Get the currently authenticated User from the SecurityContext
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); 
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated client not found"));
    }

    private ProjectResponseDto mapToResponse(Project project) {
        ProjectResponseDto response = new ProjectResponseDto();

        response.setId(project.getId());
        response.setTitle(project.getTitle());
        response.setDescription(project.getDescription());
        response.setBudget(project.getBudget());
        response.setDeadline(project.getDeadline());
        response.setCreatedAt(project.getCreatedAt());
        response.setStatus(String.valueOf(project.getStatus()));

        if (project.getClient() != null) {
            response.setClientName(project.getClient().getFullName());
        }

        return response;
    }
}

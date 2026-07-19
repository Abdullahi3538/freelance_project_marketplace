package Backend.controller;

import Backend.dto.projectdto.ProjectResponseDto;
import Backend.dto.projectdto.projectRequestDTO;
import Backend.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    // Only CLIENTS can post a project
    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ProjectResponseDto> createProject(
            @Valid @RequestBody projectRequestDTO request) {
        return new ResponseEntity<>(projectService.createProject(request), HttpStatus.CREATED);
    }

    // Any authenticated user (CLIENT, FREELANCER, ADMIN) can browse projects
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ProjectResponseDto>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    // CLIENT can get their own projects
    @GetMapping("/me")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<ProjectResponseDto>> getMyProjects() {
        return ResponseEntity.ok(projectService.getMyProjects());
    }

    // Only CLIENTS can update their own projects
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ProjectResponseDto> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody projectRequestDTO request) {
        return ResponseEntity.ok(projectService.updateProject(id, request));
    }

    // Only CLIENTS can delete their own projects
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<String> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok("Project deleted successfully");
    }
}

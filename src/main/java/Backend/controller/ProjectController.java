package Backend.controller;

import Backend.dto.projectdto.ProjectResponseDto;
import Backend.dto.projectdto.projectRequestDTO;
import Backend.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(
            ProjectService projectService) {

        this.projectService = projectService;
    }

    @PostMapping
    public ProjectResponseDto createProject(
            @Valid @RequestBody projectRequestDTO request) {

        return projectService.createProject(request);
    }

    @GetMapping
    public List<ProjectResponseDto> getAllProjects() {
        return projectService.getAllProjects();
    }

}

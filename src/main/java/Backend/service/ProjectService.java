package Backend.service;


import Backend.entity.Auth.User;
import Backend.entity.Project.Project;
import Backend.dto.projectdto.ProjectResponseDto;
import Backend.dto.projectdto.projectRequestDTO;
import Backend.repository.ProjectRepository;
import Backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProjectService {

  private ProjectRepository projectRepository;
  private UserRepository userRepository;


    public ProjectResponseDto createProject(
            projectRequestDTO request) {

        User client = userRepository.findById(
                request.getClientId()
        ).orElseThrow(
                () -> new RuntimeException("Client not found")
        );

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

    private ProjectResponseDto mapToResponse(Project project) {
        ProjectResponseDto response =
                new ProjectResponseDto();

        response.setId(project.getId());
        response.setTitle(project.getTitle());
        response.setDescription(project.getDescription());
        response.setBudget(project.getBudget());
        response.setDeadline(project.getDeadline());
        response.setCreatedAt(project.getCreatedAt());


        response.setStatus(String.valueOf(project.getStatus()));

        response.setClientName(
                project.getClient().getFullName()
        );

        return response;
    }

}

package Backend.service;


import Backend.entity.Auth.User;
import Backend.entity.Project.Project;
import Backend.dto.projectdto.ProjectResponseDto;
import Backend.dto.projectdto.projectRequestDTO;
import Backend.repository.ProjectRepository;
import Backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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

        project.setDeadline(
                LocalDate.parse(request.getDeadline())
        );

        project.setClient(client);

        projectRepository.save(project);

        ProjectResponseDto response =
                new ProjectResponseDto();

        response.setId(project.getId());
        response.setTitle(project.getTitle());
        response.setDescription(project.getDescription());
        response.setBudget(project.getBudget());
        response.setDeadline(LocalDate.parse(project.getDeadline().toString()));


        response.setStatus(String.valueOf(project.getStatus()));

        response.setClientName(
                client.getFullName()
        );

        return response;

    }

}

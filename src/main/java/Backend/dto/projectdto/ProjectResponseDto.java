package Backend.dto.projectdto;


import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ProjectResponseDto {
    private Long id;
    private String clientName;
    private String title;
    private String description;
    private Double budget;
    private LocalDate deadline;
    private String status;
    private LocalDateTime createdAt;
}


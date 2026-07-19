package Backend.dto.Milestone;

import Backend.Enmu.MilestoneStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MilestoneResponseDto {

    private Long id;
    private Long contractId;
    private String title;
    private String description;
    private LocalDate dueDate;
    private Double amount;
    private MilestoneStatus status;
}

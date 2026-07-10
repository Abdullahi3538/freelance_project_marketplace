package Backend.dto.Contractdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MilestoneRequestDto {

    private String title;
    private String description;
    private LocalDate dueDate;
    private Double amount;
}
package Backend.dto.projectdto;

import lombok.Data;

@Data
public class projectRequestDTO {
    private String title;
    private String description;
    private Double budget;
    private String deadline;
    private Long clientId;

}
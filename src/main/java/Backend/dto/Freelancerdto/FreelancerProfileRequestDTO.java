package Backend.dto.Freelancerdto;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.util.List;

@Data
public class FreelancerProfileRequestDTO {

    private String bio;

    @PositiveOrZero(message = "Experience years cannot be negative")
    private Integer experienceYears;

    @PositiveOrZero(message = "Hourly rate cannot be negative")
    private Double hourlyRate;

    private String portfolioUrl;

    // Use "skills" instead of "skillIds", accepting a list of strings
    private List<String> skills;
}

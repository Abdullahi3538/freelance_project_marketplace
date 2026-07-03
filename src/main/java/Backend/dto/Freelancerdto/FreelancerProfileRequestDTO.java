package Backend.dto.Freelancerdto;

import lombok.Data;
@Data
public class FreelancerProfileRequestDTO {

    private String bio;
    private Integer experienceYears;
    private Double hourlyRate;
    private String portfolioUrl;
    private Long userId;

}

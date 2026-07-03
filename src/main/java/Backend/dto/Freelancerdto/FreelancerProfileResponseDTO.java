package Backend.dto.Freelancerdto;


import lombok.Data;

@Data

public class FreelancerProfileResponseDTO {
    private Long id;
    private String bio;
    private Integer experienceYears;
    private Double hourlyRate;
    private String portfolioUrl;
    private String fullName;
    private String email;

}
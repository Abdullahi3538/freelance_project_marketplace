package Backend.dto.Biddto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BidRequestDTO {

    @NotNull(message = "Bid amount is required")
    @Positive(message = "Bid amount must be positive")
    private Double bidAmount;

    @NotNull(message = "Delivery days is required")
    @Positive(message = "Delivery days must be positive")
    private Integer deliveryDays;

    @NotBlank(message = "Proposal is required")
    @Size(min = 20, message = "Proposal must be at least 20 characters")
    private String proposal;

    @NotNull(message = "Project id is required")
    private Long projectId;

    // freelancerId is extracted automatically from JWT
}

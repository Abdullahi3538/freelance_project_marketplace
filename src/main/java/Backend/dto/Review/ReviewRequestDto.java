package Backend.dto.Review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {

    @NotNull(message = "Contract id is required")
    private Long contractId;

    // reviewerId is auto-extracted from JWT — no need to send it
    // revieweeId is auto-determined from the contract:
    //   - if reviewer is CLIENT  → reviewee is FREELANCER
    //   - if reviewer is FREELANCER → reviewee is CLIENT

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot be more than 5")
    private Integer rating;

    @NotBlank(message = "Comment is required")
    @Size(max = 1000, message = "Comment cannot be more than 1000 characters")
    private String comment;
}

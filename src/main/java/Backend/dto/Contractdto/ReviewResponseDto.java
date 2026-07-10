package Backend.dto.Contractdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {

    private Long id;
    private String reviewerName;
    private String revieweeName;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
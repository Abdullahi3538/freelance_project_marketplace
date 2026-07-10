package Backend.dto.Contractdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {

    private Long revieweeId;
    private Integer rating;
    private String comment;
}
package Backend.dto.Biddto;

import lombok.Data;

@Data
public class BidRequestDTO {

    private Double bidAmount;

    private Integer deliveryDays;

    private String proposal;

    private Long projectId;

    private Long freelancerId;
}

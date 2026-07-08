package Backend.dto.Biddto;
import Backend.Enmu.BidStatus;
import lombok.Data;

@Data
public class BidResponseDTO {

    private Long id;

    private Double bidAmount;

    private Integer deliveryDays;

    private String proposal;

    private BidStatus status;

    private String projectTitle;

    private String freelancerName;

}


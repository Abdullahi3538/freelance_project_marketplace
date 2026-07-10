package Backend.dto.Contractdto;

import Backend.Enmu.ContractStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractResponseDto {

    private Long id;
    private String clientName;
    private String freelancerName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double totalAmount;
    private ContractStatus status;
}
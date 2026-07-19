package Backend.repository;

import Backend.entity.Contract.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findByClientId(Long clientId);
    List<Contract> findByFreelancerId(Long freelancerId);
}

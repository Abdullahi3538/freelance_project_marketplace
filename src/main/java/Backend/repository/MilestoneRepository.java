
package Backend.repository;

import Backend.entity.Milestone.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MilestoneRepository extends JpaRepository<Milestone, Long> {
    List<Milestone> findByContractId(Long contractId);
}
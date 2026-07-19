package Backend.repository;

import Backend.entity.Bid.Bid;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByFreelancerId(Long freelancerId);
    List<Bid> findByProjectClientId(Long clientId);
}


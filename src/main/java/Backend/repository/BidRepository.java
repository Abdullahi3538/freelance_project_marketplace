package Backend.repository;

import Backend.entity.Bid.Bid;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<Bid, Long> {
}


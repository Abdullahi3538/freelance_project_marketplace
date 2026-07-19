package Backend.repository;


import Backend.entity.Freelancer.FreelancerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FreelancerProfileRepository
        extends JpaRepository<FreelancerProfile, Long> {

    boolean existsByUserId(Long userId);
    Optional<FreelancerProfile> findByUserId(Long userId);

}
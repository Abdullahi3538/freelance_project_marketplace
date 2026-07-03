package Backend.repository;


import Backend.entity.Freelancer.FreelancerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreelancerProfileRepository
        extends JpaRepository<FreelancerProfile, Long> {

    boolean existsByUserId(Long userId);

}
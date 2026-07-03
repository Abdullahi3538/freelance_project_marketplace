package Backend.repository;


import Backend.entity.Freelancer.FreelancerSkill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreelancerSkillRepository extends
        JpaRepository<FreelancerSkill, Long> {
}
package Backend.repository;

import Backend.entity.Freelancer.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends
        JpaRepository<Skill, Long> {
    boolean existsByName(String name);
}

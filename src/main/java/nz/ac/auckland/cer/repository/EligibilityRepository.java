package nz.ac.auckland.cer.repository;

import nz.ac.auckland.cer.model.categories.Eligibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EligibilityRepository extends JpaRepository<Eligibility, Integer> {
}

package nz.ac.auckland.cer.repository;

import nz.ac.auckland.cer.model.content.Feature;
import nz.ac.auckland.cer.model.content.Limitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LimitationRepository extends JpaRepository<Limitation, Integer> {
}

package nz.ac.auckland.cer.repository;

import nz.ac.auckland.cer.model.categories.Cost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CostRepository extends JpaRepository<Cost, Integer> {
}

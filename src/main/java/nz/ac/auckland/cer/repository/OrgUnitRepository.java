package nz.ac.auckland.cer.repository;

import nz.ac.auckland.cer.model.OrgUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrgUnitRepository extends JpaRepository<OrgUnit, Integer> {
}

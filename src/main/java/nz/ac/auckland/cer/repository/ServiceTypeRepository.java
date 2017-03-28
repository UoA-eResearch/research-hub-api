package nz.ac.auckland.cer.repository;

import nz.ac.auckland.cer.model.categories.LifeCycle;
import nz.ac.auckland.cer.model.categories.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType, Integer> {
}

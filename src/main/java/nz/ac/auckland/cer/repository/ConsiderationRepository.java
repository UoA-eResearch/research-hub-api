package nz.ac.auckland.cer.repository;

import nz.ac.auckland.cer.model.categories.ProductType;
import nz.ac.auckland.cer.model.content.Consideration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsiderationRepository extends JpaRepository<Consideration, Integer> {
}

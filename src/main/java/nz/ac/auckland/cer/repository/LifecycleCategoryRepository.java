package nz.ac.auckland.cer.repository;

import nz.ac.auckland.cer.model.LifecycleCategory;
import nz.ac.auckland.cer.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LifecycleCategoryRepository extends JpaRepository<LifecycleCategory, Integer> {
}

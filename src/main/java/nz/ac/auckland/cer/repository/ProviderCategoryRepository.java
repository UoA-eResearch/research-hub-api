package nz.ac.auckland.cer.repository;

import nz.ac.auckland.cer.model.ProviderCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderCategoryRepository extends JpaRepository<ProviderCategory, Integer> {
}

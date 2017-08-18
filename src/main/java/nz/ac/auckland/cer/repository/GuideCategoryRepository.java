package nz.ac.auckland.cer.repository;

import nz.ac.auckland.cer.model.GuideCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuideCategoryRepository extends JpaRepository<GuideCategory, Integer> {
}

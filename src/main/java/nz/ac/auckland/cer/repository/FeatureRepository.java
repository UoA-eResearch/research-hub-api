package nz.ac.auckland.cer.repository;

import nz.ac.auckland.cer.model.content.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeatureRepository extends JpaRepository<Feature, Integer> {
}

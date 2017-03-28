package nz.ac.auckland.cer.repository;

import nz.ac.auckland.cer.model.categories.StudyLevel;
import nz.ac.auckland.cer.model.content.Reference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferenceRepository extends JpaRepository<Reference, Integer> {
}

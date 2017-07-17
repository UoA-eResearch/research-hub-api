package nz.ac.auckland.cer.repository;

import nz.ac.auckland.cer.model.ContentSubtype;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentSubtypeRepository extends JpaRepository<ContentSubtype, Integer> {
}

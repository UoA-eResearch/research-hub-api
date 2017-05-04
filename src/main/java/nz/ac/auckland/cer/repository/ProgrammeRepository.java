package nz.ac.auckland.cer.repository;

import nz.ac.auckland.cer.model.categories.Programme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgrammeRepository extends JpaRepository<Programme, Integer> {
}
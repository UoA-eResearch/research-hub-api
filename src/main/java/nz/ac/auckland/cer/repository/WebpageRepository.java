package nz.ac.auckland.cer.repository;

import nz.ac.auckland.cer.model.Webpage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebpageRepository extends JpaRepository<Webpage, Integer> {
}

package nz.ac.auckland.cer.repository;

import nz.ac.auckland.cer.model.categories.ExternalUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalUrlRepository extends JpaRepository<ExternalUrl, Integer> {
}

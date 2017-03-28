package nz.ac.auckland.cer.repository;

import nz.ac.auckland.cer.model.Product;
import nz.ac.auckland.cer.model.content.Support;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportRepository extends JpaRepository<Support, Integer> {
}

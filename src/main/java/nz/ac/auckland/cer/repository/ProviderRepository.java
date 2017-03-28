package nz.ac.auckland.cer.repository;

import nz.ac.auckland.cer.model.categories.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Integer> {
}

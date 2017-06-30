package nz.ac.auckland.cer.repository;

import nz.ac.auckland.cer.model.ContentRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRoleRepository extends JpaRepository<ContentRole, Integer> {
}

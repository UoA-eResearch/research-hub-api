package nz.ac.auckland.cer.repository;

import nz.ac.auckland.cer.model.content.Contact;
import nz.ac.auckland.cer.model.content.Reference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {
}

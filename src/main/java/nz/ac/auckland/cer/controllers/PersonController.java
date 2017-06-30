package nz.ac.auckland.cer.controllers;

import nz.ac.auckland.cer.model.Person;
import nz.ac.auckland.cer.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@RestController
public class PersonController extends AbstractController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PersonRepository personRepository;

    public PersonController() {
        super();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/people")
    public ResponseEntity<String> getContentItems() {
        final List<Person> items = personRepository.findAll();
        String results = this.getFilteredResults(items, Person.ENTITY_NAME, "contentRoles");
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/people/{id}")
    public ResponseEntity<String> getContentItems(@PathVariable Integer id) {
        final Person item = personRepository.findOne(id);
        String results = this.getFilteredResults(item, Person.ENTITY_NAME);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }
}

package nz.ac.auckland.cer.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nz.ac.auckland.cer.model.Person;
import nz.ac.auckland.cer.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@RestController
@Api(tags={"Person"}, description="Operations on person")
public class PersonController extends AbstractController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PersonRepository personRepository;

    public PersonController() {
        super();
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/person")
    @ApiOperation(value = "get a list of people")
    public ResponseEntity<String> getPerson(@RequestParam Integer page, @RequestParam Integer size) {
        // Make sure pages greater than 0 and page sizes less than 50
        page = page < 0 ? 0 : page;
        size = size > 50 ? 50 : size;

        final Page<Person> items = personRepository.findAll(new PageRequest(page, size));
        String results = this.getFilteredResults(items, Person.ENTITY_NAME, "orgUnits",
                "contentRoles");
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/person/{id}")
    @ApiOperation(value = "get a specific person")
    public ResponseEntity<String> getPerson(@PathVariable Integer id) {
        final Person item = personRepository.findOne(id);
        String results = this.getFilteredResults(item, Person.ENTITY_NAME);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }
}

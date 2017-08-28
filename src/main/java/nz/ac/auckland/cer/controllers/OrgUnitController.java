package nz.ac.auckland.cer.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.hibernate.HibernateQuery;
import com.querydsl.jpa.impl.JPAQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nz.ac.auckland.cer.model.*;
import nz.ac.auckland.cer.repository.ContentRepository;
import nz.ac.auckland.cer.repository.OrgUnitRepository;
import nz.ac.auckland.cer.repository.PersonRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.*;
import java.util.List;


@RestController
@Api(tags={"OrgUnit"}, description="Operations on an organisational unit")
public class OrgUnitController extends AbstractController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private OrgUnitRepository orgUnitRepository;

    @Autowired
    private PersonRepository personRepository;

    public OrgUnitController() {
        super();
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/orgUnit")
    @ApiOperation(value = "get a list of org units")
    public ResponseEntity<String> getContent(@RequestParam Integer page, @RequestParam Integer size) {
        // Make sure pages greater than 0 and page sizes less than 50
        page = page < 0 ? 0 : page;
        size = size > 50 ? 50 : size;

        final Page<OrgUnit> items = orgUnitRepository.findAll(new PageRequest(page, size));
        String results = this.getFilteredResults(items, OrgUnit.ENTITY_NAME, "people", "contentItems");
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/orgUnit/{id}")
    @ApiOperation(value = "get a specific organisation unit")
    public ResponseEntity<String> getContent(@PathVariable Integer id) {
        final OrgUnit item = orgUnitRepository.findOne(id);

        SimpleFilterProvider filter = new SimpleFilterProvider();
        filter.setFailOnUnknownId(false);
        filter.addFilter(OrgUnit.ENTITY_NAME, SimpleBeanPropertyFilter.serializeAllExcept());
        filter.addFilter(Person.ENTITY_NAME, SimpleBeanPropertyFilter.serializeAllExcept("email", "username", "directoryUrl", "orgUnits", "contentRoles"));
        filter.addFilter(Content.ENTITY_NAME, SimpleBeanPropertyFilter.serializeAllExcept(Content.DETAILS));

        String results = "";

        try
        {
            results = objectMapper.writer(filter).writeValueAsString(item);
        }
        catch (JsonProcessingException e) {

        }

        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/orgUnit/{id}/userSupport")
    @ApiOperation(value = "get support people associated with an organisational unit")
    public ResponseEntity<String> getPeople(@PathVariable Integer id) {
        Query nativeQuery = entityManager.createNativeQuery(
                "SELECT DISTINCT person.id, person.title, person.first_name, person.last_name, person.email, person.username, person.job_title, person.directory_url, person.image\n" +
                        "FROM person_content_role\n" +
                        "INNER JOIN person_org_unit\n" +
                        "ON person_content_role.person_id=person_org_unit.person_id\n" +
                        "INNER JOIN person\n" +
                        "ON person_content_role.person_id=person.id\n" +
                        "WHERE person_content_role.role_type_id=3 AND person_org_unit.org_unit_id=?;", Person.class);
        nativeQuery.setParameter(1, id);

        List<Person> items = nativeQuery.getResultList();
        String result = this.getFilteredResults(items, Person.ENTITY_NAME, Person.DETAILS);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}

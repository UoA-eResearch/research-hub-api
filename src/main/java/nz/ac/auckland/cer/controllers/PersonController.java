package nz.ac.auckland.cer.controllers;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.hibernate.HibernateQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nz.ac.auckland.cer.model.Person;
import nz.ac.auckland.cer.model.QPerson;
import nz.ac.auckland.cer.repository.PersonRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@RestController
@Api(tags = {"Person"}, description = "Operations on person")
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
    @ApiOperation(value = "search for people")
    public ResponseEntity<String> getPerson(@RequestParam Integer page,
                                            @RequestParam Integer size,
                                            @RequestParam(required = false) String searchText) {

        // Make sure pages greater than 0 and page sizes less than 50
        page = page < 0 ? 0 : page;
        size = size > 50 ? 50 : size;

        Session session = entityManager.unwrap(Session.class);
        JPQLQuery<Person> queryFactory = new HibernateQuery<>(session);
        QPerson qPerson = QPerson.person;

        BooleanBuilder builder = new BooleanBuilder();

        if (searchText != null) {
            String searchTextLower = searchText.toLowerCase().trim();

            if (!searchTextLower.equals("")) {
                builder.andAnyOf(
                        qPerson.firstName.toLowerCase().contains(searchTextLower),
                        qPerson.lastName.toLowerCase().contains(searchTextLower)
                );
            }
        }

        JPQLQuery<Person> personQuery = queryFactory.from(qPerson).where(builder);
        JPQLQuery<Person> paginatedQuery = personQuery.offset(page * size).limit(size);

        int totalElements = (int) personQuery.fetchCount();
        int totalPages = (int) Math.ceil((float) totalElements / (float) size);
        int numberOfElements = (int) paginatedQuery.fetchCount();

        nz.ac.auckland.cer.model.Page<Person> hubPage = new nz.ac.auckland.cer.model.Page<>();
        hubPage.content = paginatedQuery.fetch();
        hubPage.last = (page + 1) >= totalPages;
        hubPage.totalPages = totalPages;
        hubPage.totalElements = totalElements;
//        hubPage.sort;
        hubPage.first = page == 0;
        hubPage.numberOfElements = numberOfElements;
        hubPage.size = size;
        hubPage.number = page;

        String result = this.getFilteredResults(hubPage, Person.ENTITY_NAME, "orgUnits",
                "contentRoles");

        return new ResponseEntity<>(result, HttpStatus.OK);
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

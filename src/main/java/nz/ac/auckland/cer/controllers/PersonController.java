package nz.ac.auckland.cer.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.hibernate.HibernateQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nz.ac.auckland.cer.model.*;
import nz.ac.auckland.cer.repository.PersonRepository;
import nz.ac.auckland.cer.sql.SqlParameter;
import nz.ac.auckland.cer.sql.SqlQuery;
import nz.ac.auckland.cer.sql.SqlStatement;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


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

    private static String PERSON_MATCH_SQL = "MATCH (title, first_name, last_name, job_title) AGAINST (:search_text IN BOOLEAN MODE)";

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/person")
    @ApiOperation(value = "search for people")
    public ResponseEntity<String> getPerson(@RequestParam Integer page,
                                            @RequestParam Integer size,
                                            @RequestParam(required = false) String orderBy,
                                            @RequestParam(required = false) List<Integer> orgUnits,
                                            @RequestParam(required = false) String searchText) {

        // Make sure pages greater than 0 and page sizes less than 50
        page = page < 0 ? 0 : page;
        size = size > 50 ? 50 : size;

        String searchTextProcessed = SqlQuery.preProcessSearchText(searchText);
        boolean searchOrgUnits = orgUnits != null && orgUnits.size() > 0;
        boolean searchSearchText = !searchTextProcessed.equals("");
        List<Boolean> searchConditions = new ArrayList<>();

        boolean orderByRelevance = true;
        if(orderBy != null) {
            orderByRelevance = orderBy.equals("relevance");
        }

        ArrayList<SqlStatement> statements = new ArrayList<>();

        SqlStatement countStatement = new SqlStatement("SELECT DISTINCT COUNT(*) FROM person", false);
        SqlStatement selectStatement = new SqlStatement("SELECT DISTINCT person.* FROM person", true);

        statements.add(countStatement);
        statements.add(selectStatement);

        statements.add(new SqlStatement("INNER JOIN person_content_role ON person_content_role.person_id=person.id",
                true));

        statements.add(new SqlStatement("INNER JOIN person_org_unit ON person_org_unit.person_id=person.id",
                searchOrgUnits));

        statements.add(new SqlStatement("WHERE", true));

        statements.add(new SqlStatement("person_content_role.role_type_id=3",
                true));

        statements.add(new SqlStatement("AND", searchSearchText || searchOrgUnits));

        searchConditions.add(searchSearchText);
        statements.add(new SqlStatement(PERSON_MATCH_SQL,
                searchSearchText,
                new SqlParameter<>("search_text", searchTextProcessed)));

        statements.add(new SqlStatement("AND", searchConditions.contains(true) && searchOrgUnits));

        searchConditions.add(searchOrgUnits);
        statements.add(new SqlStatement("person_org_unit.org_unit_id IN :org_units",
                searchOrgUnits,
                new SqlParameter<>("org_units", orgUnits)));

        statements.add(new SqlStatement("ORDER BY " + PERSON_MATCH_SQL + " DESC",
                searchSearchText && orderByRelevance));

        statements.add(new SqlStatement("ORDER BY person.first_name, person.last_name ASC",
                !searchSearchText || !orderByRelevance));

        SqlStatement paginationStatement = new SqlStatement("LIMIT :limit OFFSET :offset",
                true,
                new SqlParameter<>("limit", size),
                new SqlParameter<>("offset", page * size));

        // Create native queries and set parameters
        Query personPaginatedQuery = SqlQuery.generate(entityManager, statements, Person.class);

        countStatement.setExecute(true);
        selectStatement.setExecute(false);
        paginationStatement.setExecute(false);
        Query personCountQuery = SqlQuery.generate(entityManager, statements, null);

        // Get data and return results
        List<Person> paginatedResults = personPaginatedQuery.getResultList();
        int totalElements = ((BigInteger)personCountQuery.getSingleResult()).intValue();
        Page<Person> hubPage = new Page<>(paginatedResults, totalElements, orderBy, size, page);

        String result = this.getFilteredResults(hubPage, Person.ENTITY_NAME, Person.DETAILS);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/person/{id}")
    @ApiOperation(value = "get a specific person")
    public ResponseEntity<String> getPerson(@PathVariable Integer id) {
        final Person item = personRepository.findOne(id);

        SimpleFilterProvider filter = new SimpleFilterProvider();
        filter.setFailOnUnknownId(false);
        filter.addFilter(Person.ENTITY_NAME, SimpleBeanPropertyFilter.serializeAllExcept());
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
    @RequestMapping(method = RequestMethod.GET, value = "/person/{id}/userSupportContent")
    @ApiOperation(value = "get support people associated with a content")
    public ResponseEntity<String> getPeople(@PathVariable Integer id) {
        Session session = entityManager.unwrap(Session.class);
        QContentRole qContentRole = QContentRole.contentRole;
        QPerson qPerson = QPerson.person;
        QContent qContent = QContent.content;

        JPQLQuery<Person> queryFactory = new HibernateQuery<>(session);
        JPQLQuery<Content> personJPQLQuery = queryFactory.from(qContentRole).where(qContentRole.roleType.eq(new RoleType(3)).and(qContentRole.person.id.eq(id))).select(qContent);

        List<Content> content = personJPQLQuery.fetch();
        String result = this.getFilteredResults(content, Content.ENTITY_NAME, Content.DETAILS);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}

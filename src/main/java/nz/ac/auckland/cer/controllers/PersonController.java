package nz.ac.auckland.cer.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nz.ac.auckland.cer.model.Content;
import nz.ac.auckland.cer.model.ListItem;
import nz.ac.auckland.cer.model.Page;
import nz.ac.auckland.cer.model.Person;
import nz.ac.auckland.cer.repository.PersonRepository;
import nz.ac.auckland.cer.sql.SqlParameter;
import nz.ac.auckland.cer.sql.SqlQuery;
import nz.ac.auckland.cer.sql.SqlStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


@RestController
@Api(tags = {"Person"}, description = "Operations on person")
public class PersonController extends AbstractSearchController {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    static String MATCH_SQL = "MATCH (title, first_name, last_name, job_title) AGAINST (:search_text IN BOOLEAN MODE)";
    static String SELECT_SQL = "SELECT DISTINCT 'person' AS 'type', id, CONCAT(first_name, ' ', last_name) AS title, job_title AS 'subtitle', image, 'blank' AS 'url', match_sql * 2.0 AS relevance FROM person";

    private ObjectMapper objectMapper;
    private PersonRepository personRepository;

    @Autowired
    public PersonController(ObjectMapper objectMapper, PersonRepository personRepository) {
        super(SELECT_SQL, MATCH_SQL);
        this.objectMapper = objectMapper;
        this.personRepository = personRepository;
    }

    static ArrayList<SqlStatement> getSearchStatements(String searchText, List<Integer> orgUnits, List<Integer> contentItems, List<Integer> roleTypes) {
        boolean searchSearchText = !searchText.equals("");

        List<Boolean> searchConditions = new ArrayList<>();
        boolean searchOrgUnits = orgUnits != null && orgUnits.size() > 0;
        boolean searchContentItems = contentItems != null && contentItems.size() > 0;
        boolean searchRoleTypes = roleTypes != null && roleTypes.size() > 0;

        ArrayList<SqlStatement> statements = new ArrayList<>();
        statements.add(new SqlStatement("INNER JOIN person_content_role ON person_content_role.person_id=person.id",
                searchContentItems || searchRoleTypes));

        statements.add(new SqlStatement("INNER JOIN person_org_unit ON person_org_unit.person_id=person.id",
                searchOrgUnits));

        statements.add(new SqlStatement("WHERE", searchSearchText || searchOrgUnits || searchContentItems || searchRoleTypes));

        searchConditions.add(searchSearchText);
        statements.add(new SqlStatement(MATCH_SQL,
                searchSearchText,
                new SqlParameter<>("search_text", searchText)));

        statements.add(new SqlStatement("AND", searchConditions.contains(true) && searchOrgUnits));

        searchConditions.add(searchOrgUnits);
        statements.add(new SqlStatement("person_org_unit.org_unit_id IN :org_units",
                searchOrgUnits,
                new SqlParameter<>("org_units", orgUnits)));

        statements.add(new SqlStatement("AND", searchConditions.contains(true) && searchContentItems));

        searchConditions.add(searchContentItems);
        statements.add(new SqlStatement("person_content_role.content_id IN :content_items",
                searchContentItems,
                new SqlParameter<>("content_items", contentItems)));

        statements.add(new SqlStatement("AND", searchConditions.contains(true) && searchRoleTypes));

        searchConditions.add(searchRoleTypes);
        statements.add(new SqlStatement("person_content_role.role_type_id IN :role_types",
                searchRoleTypes,
                new SqlParameter<>("role_types", roleTypes)));
        return statements;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/person")
    @ApiOperation(value = "search for people")
    public Page<ListItem> getPerson(@RequestParam Integer page,
                                            @RequestParam Integer size,
                                            @RequestParam(required = false) String orderBy,
                                            @RequestParam(required = false) String searchText,
                                            @RequestParam(required = false) List<Integer> orgUnits,
                                            @RequestParam(required = false) List<Integer> contentItems,
                                            @RequestParam(required = false) List<Integer> roleTypes) throws UnsupportedEncodingException {

        String searchTextProcessed = SqlQuery.preProcessSearchText(searchText);

        return this.getSearchResults("person", page, size, orderBy, searchTextProcessed, PersonController.getSearchStatements(searchTextProcessed, orgUnits,
                contentItems, roleTypes));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/person/{id}")
    @ApiOperation(value = "get a specific person")
    public ResponseEntity<String> getPerson(@PathVariable Integer id) throws JsonProcessingException {
        final Person item = personRepository.findOne(id);

        SimpleFilterProvider filter = new SimpleFilterProvider();
        filter.setFailOnUnknownId(false);
        filter.addFilter(Person.ENTITY_NAME, SimpleBeanPropertyFilter.serializeAllExcept());
        filter.addFilter(Content.ENTITY_NAME, SimpleBeanPropertyFilter.serializeAllExcept(Content.DETAILS));
        String results = objectMapper.writer(filter).writeValueAsString(item);

        return new ResponseEntity<>(results, HttpStatus.OK);
    }
}

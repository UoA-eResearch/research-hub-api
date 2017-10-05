package nz.ac.auckland.cer.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.hibernate.HibernateQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nz.ac.auckland.cer.model.*;
import nz.ac.auckland.cer.repository.ContentRepository;
import nz.ac.auckland.cer.repository.GuideCategoryRepository;
import nz.ac.auckland.cer.sql.SqlParameter;
import nz.ac.auckland.cer.sql.SqlQuery;
import nz.ac.auckland.cer.sql.SqlStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.*;




@RestController
@Api(tags={"Content"}, description="Operations on content")
public class ContentController extends AbstractController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private GuideCategoryRepository guideCategoryRepository;

    public ContentController() {
        super();
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/content")
    @ApiOperation(value = "search for content items")
    public ResponseEntity<String> getContent(@RequestParam Integer page,
                                             @RequestParam Integer size,
                                             @RequestParam(required = false) String orderBy,
                                             @RequestParam(required = false) List<Integer> contentTypes,
                                             @RequestParam(required = false) List<Integer> researchPhases,
                                             @RequestParam(required = false) List<Integer> people,
                                             @RequestParam(required = false) List<Integer> orgUnits,
                                             @RequestParam(required = false) String searchText) {

        // Make sure pages greater than 0 and page sizes at least 1
        page = page < 0 ? 0 : page;
        size = size < 1 ? 1 : size;

        String searchTextProcessed = SqlQuery.preProcessSearchText(searchText);
        boolean searchSearchText = !searchTextProcessed.equals("");
        boolean searchContentTypes = contentTypes != null && contentTypes.size() > 0;
        boolean searchResearchPhases = researchPhases != null && researchPhases.size() > 0;
        boolean searchPeople = people != null && people.size() > 0;
        boolean searchOrgUnits = orgUnits != null && orgUnits.size() > 0;
        List<Boolean> searchConditions = new ArrayList<>();

        boolean orderByRelevance = true;
        if(orderBy != null) {
            orderByRelevance = orderBy.equals("relevance");
        }

        ArrayList<SqlStatement> statements = new ArrayList<>();

        SqlStatement countStatement = new SqlStatement("SELECT DISTINCT COUNT(*) FROM content", false);
        SqlStatement selectStatement = new SqlStatement("SELECT DISTINCT content.* FROM content", true);

        statements.add(countStatement);
        statements.add(selectStatement);

        statements.add(new SqlStatement("INNER JOIN content_content_type ON content_content_type.content_id=content.id",
                        searchContentTypes));

        statements.add(new SqlStatement("INNER JOIN content_research_phase ON content_research_phase.content_id=content.id",
                        searchResearchPhases));

        statements.add(new SqlStatement("INNER JOIN person_content_role ON person_content_role.content_id=content.id",
                        searchPeople));

        statements.add(new SqlStatement("INNER JOIN content_org_unit ON content_org_unit.content_id=content.id",
                        searchOrgUnits));

        statements.add(new SqlStatement("WHERE",searchSearchText || searchContentTypes || searchResearchPhases || searchPeople || searchOrgUnits));

        searchConditions.add(searchSearchText);
        statements.add(new SqlStatement("MATCH (name, summary, description, actionable_info, additional_info, keywords) AGAINST (:search_text IN BOOLEAN MODE)",
                searchSearchText,
                new SqlParameter<>("search_text", searchTextProcessed)));

        statements.add(new SqlStatement("AND", searchConditions.contains(true) && searchContentTypes));

        searchConditions.add(searchContentTypes);
        statements.add(new SqlStatement("content_content_type.content_type_id IN :content_types",
                        searchContentTypes,
                        new SqlParameter<>("content_types", contentTypes)));

        statements.add(new SqlStatement("AND", searchConditions.contains(true) && searchResearchPhases));

        searchConditions.add(searchResearchPhases);
        statements.add(new SqlStatement("content_research_phase.research_phase_id IN :research_phases",
                        searchResearchPhases,
                        new SqlParameter<>("research_phases", researchPhases)));

        statements.add(new SqlStatement("AND", searchConditions.contains(true) && searchPeople));

        searchConditions.add(searchPeople);
        statements.add(new SqlStatement("person_content_role.person_id IN :people AND person_content_role.role_type_id=3",
                        searchPeople,
                        new SqlParameter<>("people", people)));

        statements.add(new SqlStatement("AND", searchConditions.contains(true) && searchOrgUnits));

        searchConditions.add(searchOrgUnits);
        statements.add(new SqlStatement("content_org_unit.org_unit_id IN :org_units",
                        searchOrgUnits,
                        new SqlParameter<>("org_units", orgUnits)));

        statements.add(new SqlStatement("ORDER BY MATCH (name, summary, description, actionable_info, additional_info, keywords) AGAINST (:search_text IN BOOLEAN MODE) DESC",
                searchSearchText && orderByRelevance));

        statements.add(new SqlStatement("ORDER BY content.name ASC",
                !searchSearchText || !orderByRelevance));

        SqlStatement paginationStatement = new SqlStatement("LIMIT :limit OFFSET :offset",
                                                            true,
                                                            new SqlParameter<>("limit", size),
                                                            new SqlParameter<>("offset", page * size));
        statements.add(paginationStatement);

        // Create native queries and set parameters
        Query contentPaginatedQuery = SqlQuery.generate(entityManager, statements, Content.class);

        countStatement.setExecute(true);
        selectStatement.setExecute(false);
        paginationStatement.setExecute(false);
        Query contentCountQuery = SqlQuery.generate(entityManager, statements, null);

        // Get data and return results
        List<Content> paginatedResults = contentPaginatedQuery.getResultList();
        int totalElements = ((BigInteger)contentCountQuery.getSingleResult()).intValue();
        Page<Content> hubPage = new Page<>(paginatedResults, totalElements, orderBy, size, page);
        String result = this.getFilteredResults(hubPage, Content.ENTITY_NAME, Content.DETAILS);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/content/{id}")
    @ApiOperation(value = "get a specific content item")
    public ResponseEntity<String> getContent(@PathVariable Integer id) {
        final Content item = contentRepository.findOne(id);

        String results = "";
        boolean isGuide = false;

        for(ContentType contentType: item.getContentTypes()) {
            if(contentType.getId() == 7) {
                isGuide = true;
                break;
            }
        }

        if(isGuide) {
            SimpleFilterProvider filter = new SimpleFilterProvider();
            filter.setFailOnUnknownId(false);
            filter.addFilter(Content.ENTITY_NAME, SimpleBeanPropertyFilter.serializeAllExcept("webpages",
                    "keywords", "contentTypes", "researchPhases", "policies", "similarContentItems",
                    "actionableInfo", "callToAction", "orgUnits", "people"));
            filter.addFilter("guideCategories", SimpleBeanPropertyFilter.serializeAllExcept("contentItems"));

            try
            {
                results = objectMapper.writer(filter).writeValueAsString(item);
            }
            catch (JsonProcessingException e) {

            }
        } else {
            results = this.getFilteredResults(item, Content.ENTITY_NAME, "similarContentItems", "guideCategories");
        }
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/guideCategory/{id}")
    @ApiOperation(value = "get a specific guide")
    public ResponseEntity<String> getGuide(@PathVariable Integer id) {
        final GuideCategory item = guideCategoryRepository.findOne(id);

        String results = "";
        SimpleFilterProvider filter = new SimpleFilterProvider();
        filter.setFailOnUnknownId(false);
        filter.addFilter(GuideCategory.ENTITY_NAME, SimpleBeanPropertyFilter.serializeAllExcept("content"));
        filter.addFilter("contentItems", SimpleBeanPropertyFilter.serializeAllExcept(Content.DETAILS));

        try
        {
            results = objectMapper.writer(filter).writeValueAsString(item);
        }
        catch (JsonProcessingException e) {

        }

        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/similarContent/{id}")
    @ApiOperation(value = "get a specific content item")
    public ResponseEntity<String> getSimilarContent(@PathVariable Integer id) {
        final Content item = contentRepository.findOne(id);
        String results = this.getFilteredResults(item.getSimilarContentItems(), Content.ENTITY_NAME, Content.DETAILS);

        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/content/{id}/userSupport")
    @ApiOperation(value = "get support people associated with a content")
    public ResponseEntity<String> getPeople(@PathVariable Integer id) {
        Session session = entityManager.unwrap(Session.class);
        QContentRole qContentRole = QContentRole.contentRole;
        QPerson qPerson = QPerson.person;

        JPQLQuery<Person> queryFactory = new HibernateQuery<>(session);
        JPQLQuery<Person> personJPQLQuery = queryFactory.from(qContentRole).where(qContentRole.roleType.eq(new RoleType(3)).and(qContentRole.content.id.eq(id))).select(qPerson);

        List<Person> people = personJPQLQuery.fetch();
        String result = this.getFilteredResults(people, Person.ENTITY_NAME, Person.DETAILS);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
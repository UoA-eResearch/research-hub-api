package nz.ac.auckland.cer.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;

import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.hibernate.HibernateQuery;
import com.querydsl.jpa.impl.JPAQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nz.ac.auckland.cer.model.*;
import nz.ac.auckland.cer.repository.ContentRepository;
import nz.ac.auckland.cer.repository.GuideCategoryRepository;
import nz.ac.auckland.cer.repository.PersonRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
                                             @RequestParam(required = false) Integer[] contentTypes,
                                             @RequestParam(required = false) String searchText) {

        // Make sure pages greater than 0 and page sizes less than 50
        page = page < 0 ? 0 : page;
        size = size > 50 ? 50 : size;

        boolean searchSearchText = false;
        boolean searchContentTypes = false;
        String searchTextTrimmed = "";
        String searchTerms = "";

        if(searchText != null) {
            searchTextTrimmed = searchText.trim();
            String[] searchTokens =  searchTextTrimmed.split("\\s+");
            searchTerms = String.join("|",searchTokens);
            searchSearchText = !searchTextTrimmed.equals("");
        }

        if(contentTypes != null) {
            searchContentTypes = contentTypes.length > 0;
        }

        String contentSqlStr = "SELECT DISTINCT content.* \n" +
                "FROM content \n";

        if (searchContentTypes) {
            contentSqlStr += "INNER JOIN content_content_type ON content_content_type.content_id=content.id\n";
        }

        if(searchSearchText) {
            contentSqlStr += "INNER JOIN person_content_role ON person_content_role.content_id=content.id\n" +
                    "INNER JOIN person ON person.id=person_content_role.person_id\n" +
                    "INNER JOIN content_keyword ON content_keyword.content_id=content.id\n" +
                    "INNER JOIN content_org_unit ON content_org_unit.content_id=content.id\n" +
                    "INNER JOIN org_unit ON org_unit.id=content_org_unit.org_unit_id\n";

            contentSqlStr += "WHERE (content.name REGEXP :search_terms\n" +
                    "      OR content.summary REGEXP :search_terms\n" +
                    "      OR content.description REGEXP :search_terms\n" +
                    "      OR content.actionable_info REGEXP :search_terms\n" +
                    "      OR content.additional_info REGEXP :search_terms\n" +
                    "      OR person.first_name REGEXP :search_terms and person_content_role.role_type_id=3\n" +
                    "      OR person.last_name REGEXP :search_terms and person_content_role.role_type_id=3\n" +
                    "      OR content_keyword.keyword REGEXP :search_terms\n" +
                    "      OR org_unit.name REGEXP :search_terms)";
        }

        if (searchContentTypes) {
            if (searchSearchText) {
                contentSqlStr += "AND ";
            } else {
                contentSqlStr += "WHERE ";
            }

            contentSqlStr += "content_content_type.content_type_id IN (";
            contentSqlStr += StringUtils.join(contentTypes, ",") + ")";
        }

        Query contentNativeQuery = entityManager.createNativeQuery(contentSqlStr, Content.class);

        String contentPaginatedSqlStr = contentSqlStr + " LIMIT :limit OFFSET :offset";
        Query contentPaginatedNativeQuery = entityManager.createNativeQuery(contentPaginatedSqlStr, Content.class);

        if(searchSearchText) {
            contentNativeQuery.setParameter("search_terms", searchTerms);
            contentPaginatedNativeQuery.setParameter("search_terms", searchTerms);
        }

        contentPaginatedNativeQuery.setParameter("limit", size);
        contentPaginatedNativeQuery.setParameter("offset", page * size);

        List<Content> paginatedResults = contentPaginatedNativeQuery.getResultList();

        int totalElements = contentNativeQuery.getResultList().size(); //Not ideal because we are searching twice
        int totalPages = (int)Math.ceil((float)totalElements / (float)size);
        int numberOfElements = paginatedResults.size();

        Page<Content> hubPage = new Page<>();
        hubPage.content = paginatedResults;
        hubPage.last = (page + 1) >= totalPages;
        hubPage.totalPages = totalPages;
        hubPage.totalElements = totalElements;
//        hubPage.sort;
        hubPage.first = page == 0;
        hubPage.numberOfElements = numberOfElements;
        hubPage.size = size;
        hubPage.number = page;

        String result = this.getFilteredResults(hubPage, Content.ENTITY_NAME,"webpages",
                "keywords", "contentTypes", "orgUnits", "researchPhases", "people", "policies", "similarContentItems", "actionableInfo",
                "additionalInfo", "callToAction", "description");

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
        String results = this.getFilteredResults(item.getSimilarContentItems(), Content.ENTITY_NAME, "webpages",
                "keywords", "contentTypes", "orgUnits", "researchPhases", "people", "policies", "similarContentItems", "actionableInfo",
                "additionalInfo", "callToAction", "description", "guideCategories");

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
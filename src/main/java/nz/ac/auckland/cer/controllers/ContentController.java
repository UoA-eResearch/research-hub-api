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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@RestController
@Api(tags={"Content"}, description="Operations on content")
public class ContentController extends AbstractController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private PersonRepository personRepository;

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

        Session session = entityManager.unwrap(Session.class);
        JPQLQuery<Content> queryFactory = new HibernateQuery<>(session);
        QContent qContent = QContent.content;
        QKeyword qKeyword = QKeyword.keyword1;
        QPerson qPerson = QPerson.person;
        QOrgUnit qOrgUnit = QOrgUnit.orgUnit;

        BooleanBuilder builder = new BooleanBuilder();

        if(contentTypes != null)
        {
            ArrayList<Predicate> predicates = new ArrayList<>();

            for(Integer id : contentTypes)
            {
                predicates.add(qContent.contentTypes.contains(new ContentType(id)));
            }

            builder.andAnyOf(predicates.stream().toArray(Predicate[]::new));
        }

        if(searchText != null)
        {
            String searchTextTrimmed = searchText.trim();

            if(!searchTextTrimmed.equals("")) {
                String[] tokens = searchTextTrimmed.split(" ");
                BooleanBuilder searchTokenBuilder = new BooleanBuilder();

                for(String token: tokens) {
                    searchTokenBuilder.or(qContent.name.containsIgnoreCase(token));
                    searchTokenBuilder.or(qContent.summary.containsIgnoreCase(token));
                    searchTokenBuilder.or(qContent.description.containsIgnoreCase(token));
                    searchTokenBuilder.or(qContent.description.containsIgnoreCase(token));
                    searchTokenBuilder.or(qContent.actionableInfo.containsIgnoreCase(token));
                    searchTokenBuilder.or(qContent.additionalInfo.containsIgnoreCase(token));
                    searchTokenBuilder.or(qContent.keywords.any().keyword.containsIgnoreCase(token));
                    searchTokenBuilder.or(qContent.people.any().firstName.containsIgnoreCase(token));
                    searchTokenBuilder.or(qContent.people.any().lastName.containsIgnoreCase(token));
                    searchTokenBuilder.or(qContent.orgUnits.any().name.containsIgnoreCase(token));
                }

                builder.and(searchTokenBuilder);
            }
        }

        JPQLQuery<Content> contentQuery = queryFactory.from(qContent).where(builder);
        JPQLQuery<Content> paginatedQuery = contentQuery.offset(page*size).limit(size);

        int totalElements = (int)contentQuery.fetchCount();
        int totalPages = (int)Math.ceil((float)totalElements / (float)size);
        int numberOfElements = (int)paginatedQuery.fetchCount();

        Page<Content> hubPage = new Page<>();
        hubPage.content = paginatedQuery.fetch();
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
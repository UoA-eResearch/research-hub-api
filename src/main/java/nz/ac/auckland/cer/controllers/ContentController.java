package nz.ac.auckland.cer.controllers;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;

import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.hibernate.HibernateQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nz.ac.auckland.cer.model.*;
import nz.ac.auckland.cer.repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;


@RestController
@Api(tags={"Content"}, description="Operations on content")
public class ContentController extends AbstractController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ContentRepository contentRepository;

    public ContentController() {
        super();
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/content")
    @ApiOperation(value = "search for content items")
    public ResponseEntity<String> getContent(@RequestParam Integer page,
                                             @RequestParam Integer size,
                                             @RequestParam(required = false) Integer[] contentTypes,
                                             @RequestParam(required = false) Integer[] contentSubtypes,
                                             @RequestParam(required = false) Integer[] orgUnits,
                                             @RequestParam(required = false) Integer[] researchPhases,
                                             @RequestParam(required = false) Integer[] people,
                                             @RequestParam(required = false) String searchText) {

        // Make sure pages greater than 0 and page sizes less than 50
        page = page < 0 ? 0 : page;
        size = size > 50 ? 50 : size;

        Session session = entityManager.unwrap(Session.class);
        JPQLQuery<Content> queryFactory = new HibernateQuery<>(session);
        QContent qContent = QContent.content;
        QKeyword qKeyword = QKeyword.keyword1;

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

        if(contentSubtypes != null)
        {
            ArrayList<Predicate> predicates = new ArrayList<>();

            for(Integer id : contentSubtypes)
            {
                predicates.add(qContent.contentSubtypes.contains(new ContentSubtype(id)));
            }

            builder.andAnyOf(predicates.stream().toArray(Predicate[]::new));
        }

        if(orgUnits != null)
        {
            ArrayList<Predicate> predicates = new ArrayList<>();

            for(Integer id : orgUnits)
            {
                predicates.add(qContent.orgUnits.contains(new OrgUnit(id)));
            }

            builder.andAnyOf(predicates.stream().toArray(Predicate[]::new));
        }

        if(researchPhases != null)
        {
            ArrayList<Predicate> predicates = new ArrayList<>();

            for(Integer id : researchPhases)
            {
                predicates.add(qContent.researchPhases.contains(new ResearchPhase(id)));
            }

            builder.andAnyOf(predicates.stream().toArray(Predicate[]::new));
        }

        if(people != null)
        {
            ArrayList<Predicate> predicates = new ArrayList<>();

            for(Integer id : people)
            {
                predicates.add(qContent.people.contains(new Person(id)));
            }

            builder.andAnyOf(predicates.stream().toArray(Predicate[]::new));
        }

        if(searchText != null)
        {
            String searchTextLower = searchText.toLowerCase().trim();

            if(!searchTextLower.equals("")) {
                builder.andAnyOf(
                    qContent.name.toLowerCase().contains(searchTextLower),
                    qContent.summary.toLowerCase().contains(searchTextLower),
                    qContent.description.toLowerCase().contains(searchTextLower),
                    qContent.description.toLowerCase().contains(searchTextLower),
                    qContent.actionableInfo.toLowerCase().contains(searchTextLower),
                    qContent.additionalInfo.toLowerCase().contains(searchTextLower),
                    qContent.keywords.contains(JPAExpressions.selectFrom(qKeyword).
                                        where(qKeyword.keyword.toLowerCase().contains(searchTextLower)))
                );
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
                "keywords", "contentTypes", "contentSubtypes", "orgUnits", "researchPhases", "people");

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/content/{id}")
    @ApiOperation(value = "get a specific content item")
    public ResponseEntity<String> getContent(@PathVariable Integer id) {
        final Content item = contentRepository.findOne(id);
        String results = this.getFilteredResults(item, Content.ENTITY_NAME);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }
}

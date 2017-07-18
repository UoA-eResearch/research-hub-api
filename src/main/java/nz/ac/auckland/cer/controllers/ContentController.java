package nz.ac.auckland.cer.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nz.ac.auckland.cer.model.*;
import nz.ac.auckland.cer.repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.hibernate.HibernateQuery;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.BooleanExpression;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


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
    @ApiOperation(value = "get a list of content items")
    public ResponseEntity<String> getContent(@RequestParam Integer page, @RequestParam Integer size) {
        // Make sure pages greater than 0 and page sizes less than 50
        page = page < 0 ? 0 : page;
        size = size > 50 ? 50 : size;

        final Page<Content> items = contentRepository.findAll(new PageRequest(page, size));
        String results = this.getFilteredResults(items, Content.ENTITY_NAME,"webpages",
                "keywords", "contentTypes", "contentSubtypes", "orgUnits", "researchPhases", "people");
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/content/{id}")
    @ApiOperation(value = "get a specific content item")
    public ResponseEntity<String> getContent(@PathVariable Integer id) {
        final Content item = contentRepository.findOne(id);
        String results = this.getFilteredResults(item, Content.ENTITY_NAME);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/content/search")
    @ApiOperation(value = "search for content items")
    public ResponseEntity<String> getContent(@RequestParam(required = false) Integer[] contentTypes,
                                             @RequestParam(required = false) Integer[] contentSubtypes,
                                             @RequestParam(required = false) Integer[] orgUnits,
                                             @RequestParam(required = false) Integer[] researchPhases,
                                             @RequestParam(required = false) Integer[] people,
                                             @RequestParam(required = false) String searchText) {


        Session session = entityManager.unwrap(Session.class);
        JPQLQuery query = new HibernateQuery(session);
        QContent qContent = QContent.content;

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
                        qContent.additionalInfo.toLowerCase().contains(searchTextLower)
                );
            }

            // TODO: search keywords
            // TODO: paginate
        }

        List<Content> items = query.from(qContent)
                .where(builder)
                .list(qContent);

        String result = this.getFilteredResults(items, Content.ENTITY_NAME,"webpages",
                "keywords", "contentTypes", "contentSubtypes", "orgUnits", "researchPhases", "people");

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}

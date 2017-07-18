package nz.ac.auckland.cer.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nz.ac.auckland.cer.model.Content;
import nz.ac.auckland.cer.repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}

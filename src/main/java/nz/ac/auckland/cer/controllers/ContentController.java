package nz.ac.auckland.cer.controllers;

import nz.ac.auckland.cer.model.Content;
import nz.ac.auckland.cer.repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@RestController
public class ContentController extends AbstractController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ContentRepository contentRepository;

    public ContentController() {
        super();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/content")
    public ResponseEntity<String> getContentItems() {
        final List<Content> contentItems = contentRepository.findAll();
        String results = this.getFilteredResults(contentItems, Content.ENTITY_NAME, "researchPhases", "externalUrls");
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/content/{id}")
    public ResponseEntity<String> getContentItems(@PathVariable Integer id) {
        final Content content = contentRepository.findOne(id);
        String results = this.getFilteredResults(content, Content.ENTITY_NAME);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }
}

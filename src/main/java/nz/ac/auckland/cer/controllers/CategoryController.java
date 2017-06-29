package nz.ac.auckland.cer.controllers;


import nz.ac.auckland.cer.model.categories.ContentType;
import nz.ac.auckland.cer.model.categories.ResearchPhase;
import nz.ac.auckland.cer.repository.ResearchPhaseRepository;
import nz.ac.auckland.cer.repository.ContentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;


@RestController
public class CategoryController extends AbstractController {

    @Autowired
    private ContentTypeRepository contentTypeRepository;

    @Autowired
    private ResearchPhaseRepository researchPhaseRepository;

    public CategoryController() {
        super();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/category/contentType")
    public ResponseEntity<String> getContentTypes() {
        final List<ContentType> contentTypes = contentTypeRepository.findAll();
        String results = this.getFilteredResults(contentTypes, ContentType.ENTITY_NAME, "contentItems");
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/category/researchPhase")
    public ResponseEntity<String> getResearchPhases() {
        final List<ResearchPhase> contentTypes = researchPhaseRepository.findAll();
        String results = this.getFilteredResults(contentTypes, ResearchPhase.ENTITY_NAME, "contentItems");
        return new ResponseEntity<>(results, HttpStatus.OK);
    }
}

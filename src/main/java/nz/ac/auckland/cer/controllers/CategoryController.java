package nz.ac.auckland.cer.controllers;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nz.ac.auckland.cer.model.categories.ContentType;
import nz.ac.auckland.cer.model.categories.ResearchPhase;
import nz.ac.auckland.cer.model.categories.Role;
import nz.ac.auckland.cer.repository.ResearchPhaseRepository;
import nz.ac.auckland.cer.repository.ContentTypeRepository;
import nz.ac.auckland.cer.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;


@RestController
@Api(tags={"Category"}, description="Operations on category")
public class CategoryController extends AbstractController {

    @Autowired
    private ContentTypeRepository contentTypeRepository;

    @Autowired
    private ResearchPhaseRepository researchPhaseRepository;

    @Autowired
    private RoleRepository roleRepository;

    public CategoryController() {
        super();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/category/contentType")
    @ApiOperation(value = "get all content types")
    public ResponseEntity<String> getContentType() {
        final List<ContentType> items = contentTypeRepository.findAll();
        String results = this.getFilteredResults(items, ContentType.ENTITY_NAME, "contentItems");
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/category/researchPhase")
    @ApiOperation(value = "get all research phases")
    public ResponseEntity<String> getResearchPhase() {
        final List<ResearchPhase> items = researchPhaseRepository.findAll();
        String results = this.getFilteredResults(items, ResearchPhase.ENTITY_NAME, "contentItems");
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/category/roleType")
    @ApiOperation(value = "get all role types")
    public ResponseEntity<String> getRoleType() {
        final List<Role> items = roleRepository.findAll();
        String results = this.getFilteredResults(items, Role.ENTITY_NAME, "contentRoles");
        return new ResponseEntity<>(results, HttpStatus.OK);
    }
}

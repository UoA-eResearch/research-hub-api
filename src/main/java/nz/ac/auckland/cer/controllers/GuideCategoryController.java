package nz.ac.auckland.cer.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nz.ac.auckland.cer.model.Content;
import nz.ac.auckland.cer.model.GuideCategory;
import nz.ac.auckland.cer.repository.GuideCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@Api(tags={"GuideCategory"}, description="Operations on guide categories")
public class GuideCategoryController extends AbstractController {

    @Autowired
    private GuideCategoryRepository guideCategoryRepository;

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
}

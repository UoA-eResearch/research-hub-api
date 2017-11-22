package nz.ac.auckland.cer.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nz.ac.auckland.cer.model.GuideCategory;
import nz.ac.auckland.cer.repository.GuideCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@Api(tags={"GuideCategory"}, description="Operations on guide categories")
public class GuideCategoryController {

    private static final Logger logger = LoggerFactory.getLogger(GuideCategoryController.class);

    private ObjectMapper objectMapper;
    private GuideCategoryRepository guideCategoryRepository;

    @Autowired
    public GuideCategoryController(ObjectMapper objectMapper, GuideCategoryRepository guideCategoryRepository) {
        this.objectMapper = objectMapper;
        this.guideCategoryRepository = guideCategoryRepository;
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/guideCategory/{id}")
    @ApiOperation(value = "get a specific guide")
    public ResponseEntity<String> getGuideCategory(@PathVariable Integer id) {
        final GuideCategory item = guideCategoryRepository.findOne(id);

        String results = "";
        SimpleFilterProvider filter = new SimpleFilterProvider();
        filter.setFailOnUnknownId(false);
        filter.addFilter("content", SimpleBeanPropertyFilter.filterOutAllExcept("id", "name"));
        filter.addFilter("contentItems", SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "summary", "image"));

        try
        {
            results = objectMapper.writer(filter).writeValueAsString(item);
        }
        catch (JsonProcessingException e) {
            logger.error(e.toString());
        }

        return new ResponseEntity<>(results, HttpStatus.OK);
    }
}

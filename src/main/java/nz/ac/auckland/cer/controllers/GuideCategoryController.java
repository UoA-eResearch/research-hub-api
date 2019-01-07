package nz.ac.auckland.cer.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(tags={"GuideCategory"}, description="Operations on guide categories")
public class GuideCategoryController {

    private ObjectMapper objectMapper;
    private GuideCategoryRepository guideCategoryRepository;

    @Autowired
    public GuideCategoryController(ObjectMapper objectMapper, GuideCategoryRepository guideCategoryRepository) {
        this.objectMapper = objectMapper;
        this.guideCategoryRepository = guideCategoryRepository;
    }


    @RequestMapping(method = RequestMethod.GET, value = "/guideCategory/{id}")
    @ApiOperation(value = "get a specific guide")
    public ResponseEntity<String> getGuideCategory(@PathVariable Integer id) throws JsonProcessingException {
        final GuideCategory item = guideCategoryRepository.findOne(id);

        SimpleFilterProvider filter = new SimpleFilterProvider();
        filter.setFailOnUnknownId(false);
        filter.addFilter(GuideCategory.ENTITY_NAME, SimpleBeanPropertyFilter.serializeAllExcept("contentItems"));
        filter.addFilter("content", SimpleBeanPropertyFilter.filterOutAllExcept("id", "name"));
        String results = objectMapper.writer(filter).writeValueAsString(item);

        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/guideCategory/{id}/contentItems")
    @ApiOperation(value = "get a specific content item")
    public ResponseEntity<String> getGuideCategoryContentItems(@PathVariable Integer id) throws JsonProcessingException {
        final GuideCategory item = guideCategoryRepository.findOne(id);

        SimpleFilterProvider filter = new SimpleFilterProvider();
        filter.setFailOnUnknownId(false);
        filter.addFilter(Content.ENTITY_NAME, SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "image"));
        String results = objectMapper.writer(filter).writeValueAsString(item.getContentItems());

        return new ResponseEntity<>(results, HttpStatus.OK);
    }
}

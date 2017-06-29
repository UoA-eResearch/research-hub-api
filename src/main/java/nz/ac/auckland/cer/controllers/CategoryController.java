package nz.ac.auckland.cer.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import nz.ac.auckland.cer.model.Content;
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

import java.util.Collection;
import java.util.List;


@RestController
public class CategoryController {


    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ContentTypeRepository contentTypeRepository;


    @Autowired
    private ResearchPhaseRepository researchPhaseRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/category/contentType")
    public ResponseEntity<String> getContentTypes() {
        final List<ContentType> contentTypes = contentTypeRepository.findAll();

        SimpleFilterProvider filter = new SimpleFilterProvider();
        filter.setFailOnUnknownId(false);
        filter.addFilter(ContentType.ENTITY_NAME, SimpleBeanPropertyFilter.serializeAllExcept("contentItems"));
        String result = "";

        try
        {
            result = objectMapper.writer(filter).writeValueAsString(contentTypes);
        }
        catch (JsonProcessingException e) {

        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/category/researchPhase")
    public ResponseEntity<String> getResearchPhases() {
        final List<ResearchPhase> contentTypes = researchPhaseRepository.findAll();

        SimpleFilterProvider filter = new SimpleFilterProvider();
        filter.setFailOnUnknownId(false);
        filter.addFilter(ResearchPhase.ENTITY_NAME, SimpleBeanPropertyFilter.serializeAllExcept("contentItems"));
        String result = "";

        try
        {
            result = objectMapper.writer(filter).writeValueAsString(contentTypes);
        }
        catch (JsonProcessingException e) {

        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}

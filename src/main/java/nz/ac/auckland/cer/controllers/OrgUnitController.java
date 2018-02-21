package nz.ac.auckland.cer.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nz.ac.auckland.cer.model.Content;
import nz.ac.auckland.cer.model.OrgUnit;
import nz.ac.auckland.cer.model.Person;
import nz.ac.auckland.cer.repository.OrgUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@Api(tags={"OrgUnit"}, description="Operations on an organisational unit")
public class OrgUnitController {

    private ObjectMapper objectMapper;
    private OrgUnitRepository orgUnitRepository;

    @Autowired
    public OrgUnitController(ObjectMapper objectMapper, OrgUnitRepository orgUnitRepository) {
        this.objectMapper = objectMapper;
        this.orgUnitRepository = orgUnitRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/orgUnit")
    @ApiOperation(value = "get a list of org units")
    public ResponseEntity<String> getOrgUnit(@RequestParam Integer page, @RequestParam Integer size) throws JsonProcessingException {
        // Make sure pages greater than 0 and page sizes at least 1
        page = page < 0 ? 0 : page;
        size = size < 1 ? 1 : size;

        final Page<OrgUnit> items = orgUnitRepository.findAll(new PageRequest(page, size));

        SimpleFilterProvider filter = new SimpleFilterProvider();
        filter.setFailOnUnknownId(false);
        filter.addFilter(OrgUnit.ENTITY_NAME, SimpleBeanPropertyFilter.serializeAllExcept("people", "contentItems"));
        String results = objectMapper.writer(filter).writeValueAsString(items);

        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/orgUnit/{id}")
    @ApiOperation(value = "get a specific organisation unit")
    public ResponseEntity<String> getOrgUnit(@PathVariable Integer id) throws JsonProcessingException {
        final OrgUnit item = orgUnitRepository.findOne(id);

        SimpleFilterProvider filter = new SimpleFilterProvider();
        filter.setFailOnUnknownId(false);
        filter.addFilter(OrgUnit.ENTITY_NAME, SimpleBeanPropertyFilter.serializeAllExcept());
        filter.addFilter(Person.ENTITY_NAME, SimpleBeanPropertyFilter.serializeAllExcept("email", "username", "directoryUrl", "orgUnits", "contentRoles"));
        filter.addFilter(Content.ENTITY_NAME, SimpleBeanPropertyFilter.serializeAllExcept(Content.DETAILS));
        String results = objectMapper.writer(filter).writeValueAsString(item);

        return new ResponseEntity<>(results, HttpStatus.OK);
    }
}

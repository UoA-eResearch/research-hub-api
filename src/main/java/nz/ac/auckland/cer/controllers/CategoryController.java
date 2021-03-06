package nz.ac.auckland.cer.controllers;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nz.ac.auckland.cer.model.ContentType;
import nz.ac.auckland.cer.model.ResearchPhase;
import nz.ac.auckland.cer.model.RoleType;
import nz.ac.auckland.cer.repository.ContentTypeRepository;
import nz.ac.auckland.cer.repository.ResearchPhaseRepository;
import nz.ac.auckland.cer.repository.RoleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.MediaType;
import java.util.List;


@RestController
@RequestMapping(produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(tags = {"Category"}, description = "Operations on category")
public class CategoryController {

    private ContentTypeRepository contentTypeRepository;
    private ResearchPhaseRepository researchPhaseRepository;
    private RoleTypeRepository roleTypeRepository;

    @Autowired
    public CategoryController(ContentTypeRepository contentTypeRepository, ResearchPhaseRepository researchPhaseRepository, RoleTypeRepository roleTypeRepository) {
        this.contentTypeRepository = contentTypeRepository;
        this.researchPhaseRepository = researchPhaseRepository;
        this.roleTypeRepository = roleTypeRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/category/contentType")
    @ApiOperation(value = "get all content types")
    public List<ContentType> getContentType() {
        return contentTypeRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/category/researchPhase")
    @ApiOperation(value = "get all research phases")
    public List<ResearchPhase> getResearchPhase() {
        return researchPhaseRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/category/roleType")
    @ApiOperation(value = "get all role types")
    public List<RoleType> getRoleType() {
        return roleTypeRepository.findAll();
    }
}

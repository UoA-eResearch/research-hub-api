package nz.ac.auckland.cer.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nz.ac.auckland.cer.model.Content;
import nz.ac.auckland.cer.model.ContentType;
import nz.ac.auckland.cer.model.ListItem;
import nz.ac.auckland.cer.model.Page;
import nz.ac.auckland.cer.repository.ContentRepository;
import nz.ac.auckland.cer.sql.SqlParameter;
import nz.ac.auckland.cer.sql.SqlQuery;
import nz.ac.auckland.cer.sql.SqlStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@Api(tags={"Content"}, description="Operations on content")
public class ContentController extends AbstractSearchController {

    private static final Logger logger = LoggerFactory.getLogger(ContentController.class);

    private ObjectMapper objectMapper;
    private ContentRepository contentRepository;

    static String SELECT_SQL = "SELECT DISTINCT 'content' AS 'type', id, name AS 'title', summary AS 'subtitle', image, 'blank' AS 'url', match_sql * 100.0 AS relevance FROM content";
    static String MATCH_SQL = "MATCH (name, summary, description, actionable_info, additional_info, keywords) AGAINST (:search_text IN BOOLEAN MODE)";

    @Autowired
    public ContentController(ObjectMapper objectMapper, ContentRepository contentRepository) {
        super(SELECT_SQL, MATCH_SQL);
        this.objectMapper = objectMapper;
        this.contentRepository = contentRepository;
    }

    static ArrayList<SqlStatement> getSearchStatements(String searchText, List<Integer> contentTypes, List<Integer> researchPhases,
                                                         List<Integer> people, List<Integer> roleTypes, List<Integer> orgUnits) {
        String searchTextProcessed = SqlQuery.preProcessSearchText(searchText);
        boolean searchSearchText = !searchTextProcessed.equals("");

        boolean searchContentTypes = contentTypes != null && contentTypes.size() > 0;
        boolean searchResearchPhases = researchPhases != null && researchPhases.size() > 0;
        boolean searchPeople = people != null && people.size() > 0;
        boolean searchRoleTypes = searchPeople && (roleTypes != null && roleTypes.size() > 0);
        boolean searchOrgUnits = orgUnits != null && orgUnits.size() > 0;

        ArrayList<SqlStatement> statements = new ArrayList<>();
        List<Boolean> searchConditions = new ArrayList<>();

        statements.add(new SqlStatement("INNER JOIN content_content_type ON content_content_type.content_id=content.id",
                searchContentTypes));

        statements.add(new SqlStatement("INNER JOIN content_research_phase ON content_research_phase.content_id=content.id",
                searchResearchPhases));

        statements.add(new SqlStatement("INNER JOIN person_content_role ON person_content_role.content_id=content.id",
                searchPeople));

        statements.add(new SqlStatement("INNER JOIN content_org_unit ON content_org_unit.content_id=content.id",
                searchOrgUnits));

        statements.add(new SqlStatement("WHERE",searchSearchText || searchContentTypes || searchResearchPhases || searchPeople || searchOrgUnits));

        searchConditions.add(searchSearchText);
        statements.add(new SqlStatement(MATCH_SQL,
                searchSearchText,
                new SqlParameter<>("search_text", searchTextProcessed)));

        statements.add(new SqlStatement("AND", searchConditions.contains(true) && searchContentTypes));

        searchConditions.add(searchContentTypes);
        statements.add(new SqlStatement("content_content_type.content_type_id IN :content_types",
                searchContentTypes,
                new SqlParameter<>("content_types", contentTypes)));

        statements.add(new SqlStatement("AND", searchConditions.contains(true) && searchResearchPhases));

        searchConditions.add(searchResearchPhases);
        statements.add(new SqlStatement("content_research_phase.research_phase_id IN :research_phases",
                searchResearchPhases,
                new SqlParameter<>("research_phases", researchPhases)));

        statements.add(new SqlStatement("AND", searchConditions.contains(true) && searchPeople));

        searchConditions.add(searchPeople);
        statements.add(new SqlStatement("person_content_role.person_id IN :people",
                searchPeople,
                new SqlParameter<>("people", people)));

        statements.add(new SqlStatement("AND", searchConditions.contains(true) && searchRoleTypes));

        searchConditions.add(searchRoleTypes);
        statements.add(new SqlStatement("person_content_role.role_type_id IN :role_types",
                searchRoleTypes,
                new SqlParameter<>("role_types", roleTypes)));

        statements.add(new SqlStatement("AND", searchConditions.contains(true) && searchOrgUnits));

        searchConditions.add(searchOrgUnits);
        statements.add(new SqlStatement("content_org_unit.org_unit_id IN :org_units",
                searchOrgUnits,
                new SqlParameter<>("org_units", orgUnits)));

        return statements;
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/content")
    @ApiOperation(value = "search for content items")
    public Page<ListItem> getContent(@RequestParam Integer page,
                                             @RequestParam Integer size,
                                             @RequestParam(required = false) String orderBy,
                                             @RequestParam(required = false) String searchText,
                                             @RequestParam(required = false) List<Integer> contentTypes,
                                             @RequestParam(required = false) List<Integer> researchPhases,
                                             @RequestParam(required = false) List<Integer> people,
                                             @RequestParam(required = false) List<Integer> roleTypes,
                                             @RequestParam(required = false) List<Integer> orgUnits) {

        return this.getSearchResults("content", page, size, orderBy, searchText, ContentController.getSearchStatements(searchText, contentTypes,
                researchPhases, people, roleTypes, orgUnits));
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/content/{id}")
    @ApiOperation(value = "get a specific content item")
    public ResponseEntity<String> getContent(@PathVariable Integer id) {
        final Content item = contentRepository.findOne(id);

        String results = "";
        boolean isGuide = false;

        for(ContentType contentType: item.getContentTypes()) {
            if(contentType.getId() == 7) {
                isGuide = true;
                break;
            }
        }

        SimpleFilterProvider filter = new SimpleFilterProvider();
        filter.setFailOnUnknownId(false);

        if(isGuide) {

            filter.addFilter(Content.ENTITY_NAME, SimpleBeanPropertyFilter.serializeAllExcept("webpages",
                    "keywords", "researchPhases", "policies", "similarContentItems",
                    "actionableInfo", "callToAction", "orgUnits", "people"));
            filter.addFilter("guideCategories", SimpleBeanPropertyFilter.serializeAllExcept("contentItems"));
        } else {
            filter.addFilter(Content.ENTITY_NAME, SimpleBeanPropertyFilter.serializeAllExcept("similarContentItems", "guideCategories"));
        }

        try
        {
            results = objectMapper.writer(filter).writeValueAsString(item);
        }
        catch (JsonProcessingException e) {
            logger.error(e.toString());
        }

        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/content/{id}/similar")
    @ApiOperation(value = "get a specific content item")
    public ResponseEntity<String> getSimilarContent(@PathVariable Integer id) {
        final Content item = contentRepository.findOne(id);
//        String results = this.getFilteredResults(item.getSimilarContentItems(), Content.ENTITY_NAME, Content.DETAILS);

        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
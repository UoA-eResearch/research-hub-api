package nz.ac.auckland.cer.controllers;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nz.ac.auckland.cer.model.Content;
import nz.ac.auckland.cer.model.Page;
import nz.ac.auckland.cer.model.SearchResult;
import nz.ac.auckland.cer.sql.SqlParameter;
import nz.ac.auckland.cer.sql.SqlQuery;
import nz.ac.auckland.cer.sql.SqlStatement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;



@RestController
@Api(tags={"Search"}, description="Site wide search")
public class SearchController extends AbstractController {



    @PersistenceContext
    private EntityManager entityManager;

    SearchController() {
        super();
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/search")
    @ApiOperation(value = "search for content items")
    public Page<SearchResult> getSearchResults(@RequestParam Integer page,
                                             @RequestParam Integer size,
                                             @RequestParam(required = false) String orderBy,
                                             @RequestParam(required = false) List<Integer> people,
                                             @RequestParam(required = false) List<Integer> orgUnits,
                                             @RequestParam(required = false) List<Integer> researchPhases,
                                             @RequestParam(required = false) String searchText) {

        String searchTextProcessed = SqlQuery.preProcessSearchText(searchText);
        boolean searchSearchText = !searchTextProcessed.equals("");

        boolean orderByRelevance = true;
        if(orderBy != null) {
            orderByRelevance = orderBy.equals("relevance");
        }

        ArrayList<SqlStatement> statements = new ArrayList<>();
        ArrayList<Integer> roleTypes = new ArrayList<>();
        roleTypes.add(3);

        SqlStatement countStatement = new SqlStatement("SELECT DISTINCT COUNT(*) FROM (", false);
        SqlStatement selectStatement = new SqlStatement("SELECT DISTINCT * FROM (", true);
        statements.add(countStatement);
        statements.add(selectStatement);

        statements.add(new SqlStatement("SELECT DISTINCT 'content' AS 'type', id, name AS 'title', summary AS 'subtitle', image, " + ContentController.CONTENT_MATCH_SQL +" AS relevance FROM content", true, new SqlParameter<>("search_text", searchTextProcessed)));
        statements.addAll(ContentController.getSearchQuery(new ArrayList<>(), researchPhases, people, roleTypes, orgUnits, searchTextProcessed));
        statements.add(new SqlStatement("UNION", true));

        statements.add(new SqlStatement("SELECT DISTINCT 'person' AS 'type', id, CONCAT(first_name, ' ', last_name) AS title, job_title AS 'subtitle', image, " + PersonController.PERSON_MATCH_SQL +" AS relevance FROM person", true, new SqlParameter<>("search_text", searchTextProcessed)));
        statements.addAll(PersonController.getSearchQuery(orgUnits, new ArrayList<>(), roleTypes, searchTextProcessed));
        statements.add(new SqlStatement("UNION", true));

        statements.add(new SqlStatement("SELECT DISTINCT 'policy' AS 'type', id, name AS 'title', description AS 'subtitle', 'blank' AS 'image', " + PolicyController.POLICY_MATCH_SQL + " as relevance FROM policy", true, new SqlParameter<>("search_text", searchTextProcessed)));
        statements.addAll(PolicyController.getSearchQuery(searchTextProcessed));

        statements.add(new SqlStatement(") AS sitewide", true));

        statements.add(new SqlStatement("ORDER BY relevance DESC", searchSearchText && orderByRelevance));
        statements.add(new SqlStatement("ORDER BY title ASC", !searchSearchText || !orderByRelevance));

        SqlStatement paginationStatement = new SqlStatement("LIMIT :limit OFFSET :offset",
                true,
                new SqlParameter<>("limit", size),
                new SqlParameter<>("offset", page * size));

        // Create native queries and set parameters
        Query contentPaginatedQuery = SqlQuery.generate(entityManager, statements, null, "SearchResult");

        countStatement.setExecute(true);
        selectStatement.setExecute(false);
        paginationStatement.setExecute(false);
        Query contentCountQuery = SqlQuery.generate(entityManager, statements, null, null);

        // Get data and return results
        List<SearchResult> paginatedResults = contentPaginatedQuery.getResultList();
        int totalElements = ((BigInteger)contentCountQuery.getSingleResult()).intValue();
        return new Page<SearchResult>(paginatedResults, totalElements, orderBy, size, page);
    }

}

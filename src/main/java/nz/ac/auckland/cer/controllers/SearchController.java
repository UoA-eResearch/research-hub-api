package nz.ac.auckland.cer.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nz.ac.auckland.cer.model.ListItem;
import nz.ac.auckland.cer.model.Page;
import nz.ac.auckland.cer.sql.SqlParameter;
import nz.ac.auckland.cer.sql.SqlQuery;
import nz.ac.auckland.cer.sql.SqlStatement;
import org.springframework.web.bind.annotation.*;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//import static nz.ac.auckland.cer.controllers.ContentController.CONTENT_SELECT_SQL;
import static nz.ac.auckland.cer.controllers.ContentController.CONTENT_SELECT_SQL;
import static nz.ac.auckland.cer.controllers.PersonController.PERSON_SELECT_SQL;
import static nz.ac.auckland.cer.controllers.PolicyController.POLICY_SELECT_SQL;


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
    public Page<ListItem> getSearchResults(@RequestParam Integer page,
                                           @RequestParam Integer size,
                                           @RequestParam(required = false) String orderBy,
                                           @RequestParam(required = false) String objectType,
                                           @RequestParam(required = false) List<Integer> people,
                                           @RequestParam(required = false) List<Integer> orgUnits,
                                           @RequestParam(required = false) List<Integer> researchPhases,
                                           @RequestParam(required = false) List<Integer> contentItems,
                                           @RequestParam(required = false) List<Integer> roleTypes,
                                           @RequestParam(required = false) List<Integer> contentTypes,
                                           @RequestParam(required = false) String searchText) {

        String searchTextProcessed = SqlQuery.preProcessSearchText(searchText);
        boolean searchSearchText = !searchTextProcessed.equals("");

        boolean orderByRelevance = true;
        if(orderBy != null) {
            orderByRelevance = orderBy.equals("relevance");
        }

        List<String> objectTypes = Arrays.asList("content", "person", "policy");
        boolean searchAll = objectType == null || !objectTypes.contains(objectType);

        ArrayList<SqlStatement> statements = new ArrayList<>();

        SqlStatement countStatement = new SqlStatement("SELECT DISTINCT COUNT(*) FROM (", false);
        SqlStatement selectStatement = new SqlStatement("SELECT DISTINCT * FROM (", true);
        statements.add(countStatement);
        statements.add(selectStatement);

        List<Boolean> searchConditions = new ArrayList<>();

        if (searchAll || objectType.equals("content")) {
            statements.add(new SqlStatement(CONTENT_SELECT_SQL, true, new SqlParameter<>("search_text", searchTextProcessed))); //"SELECT DISTINCT " + ContentController.getSelectSql(searchSearchText) + " FROM content"
            statements.addAll(ContentController.getSearchQuery(contentTypes, researchPhases, people, roleTypes, orgUnits, searchTextProcessed));
            searchConditions.add(true);
        }

        if (searchAll || objectType.equals("person")) {
            statements.add(new SqlStatement("UNION", searchConditions.contains(true)));
            statements.add(new SqlStatement(PERSON_SELECT_SQL, true, new SqlParameter<>("search_text", searchTextProcessed)));
            statements.addAll(PersonController.getSearchQuery(orgUnits, contentItems, roleTypes, searchTextProcessed));
            searchConditions.add(true);
        }

        if (searchAll || objectType.equals("policy")) {
            statements.add(new SqlStatement("UNION", searchConditions.contains(true)));
            statements.add(new SqlStatement(POLICY_SELECT_SQL, true, new SqlParameter<>("search_text", searchTextProcessed)));
            statements.addAll(PolicyController.getSearchQuery(searchTextProcessed));
        }

        statements.add(new SqlStatement(") AS sitewide", true));

        statements.add(new SqlStatement("ORDER BY relevance DESC", searchSearchText && orderByRelevance));
        statements.add(new SqlStatement("ORDER BY title ASC", !searchSearchText || !orderByRelevance));

        SqlStatement paginationStatement = new SqlStatement("LIMIT :limit OFFSET :offset",
                true,
                new SqlParameter<>("limit", size),
                new SqlParameter<>("offset", page * size));

        // Create native queries and set parameters
        Query contentPaginatedQuery = SqlQuery.generate(entityManager, statements, null, "ListItem");

        countStatement.setExecute(true);
        selectStatement.setExecute(false);
        paginationStatement.setExecute(false);
        Query contentCountQuery = SqlQuery.generate(entityManager, statements, null, null);

        // Get data and return results
        List<ListItem> paginatedResults = contentPaginatedQuery.getResultList();
        int totalElements = ((BigInteger)contentCountQuery.getSingleResult()).intValue();
        return new Page<ListItem>(paginatedResults, totalElements, orderBy, size, page);
    }

}

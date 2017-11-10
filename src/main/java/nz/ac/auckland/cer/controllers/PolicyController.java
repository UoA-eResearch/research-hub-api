package nz.ac.auckland.cer.controllers;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.hibernate.HibernateQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nz.ac.auckland.cer.model.*;
import nz.ac.auckland.cer.repository.PolicyRepository;
import nz.ac.auckland.cer.sql.SqlParameter;
import nz.ac.auckland.cer.sql.SqlQuery;
import nz.ac.auckland.cer.sql.SqlStatement;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


@RestController
@Api(tags = {"Person"}, description = "Operations on person")
public class PolicyController extends AbstractController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PolicyRepository policyRepository;

    public PolicyController() {
        super();
    }

    public static String POLICY_MATCH_SQL = "MATCH (name, description) AGAINST (:search_text IN BOOLEAN MODE)";
    public static String POLICY_SELECT_SQL = "SELECT DISTINCT 'policy' AS 'type', id, name AS 'title', description AS 'subtitle', 'blank' AS 'image', url AS 'url', " + PolicyController.POLICY_MATCH_SQL + " as relevance FROM policy";

    public static ArrayList<SqlStatement> getSearchQuery(String searchText) {
        boolean searchSearchText = !searchText.equals("");
        ArrayList<SqlStatement> statements = new ArrayList<>();
        statements.add(new SqlStatement("WHERE", searchSearchText));
        statements.add(new SqlStatement(POLICY_MATCH_SQL, searchSearchText, new SqlParameter<>("search_text", searchText)));
        return statements;
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/policy")
    @ApiOperation(value = "search for policies")
    public Page<ListItem> getPolicy(@RequestParam Integer page,
                                    @RequestParam Integer size,
                                    @RequestParam(required = false) String orderBy,
                                    @RequestParam(required = false) String searchText) {

        // Make sure pages greater than 0 and page sizes at least 1
        page = page < 0 ? 0 : page;
        size = size < 1 ? 1 : size;

        String searchTextProcessed = SqlQuery.preProcessSearchText(searchText);
        boolean searchSearchText = !searchTextProcessed.equals("");

        boolean orderByRelevance = true;
        if(orderBy != null) {
            orderByRelevance = orderBy.equals("relevance");
        }

        ArrayList<SqlStatement> statements = new ArrayList<>();

        SqlStatement countStatement = new SqlStatement("SELECT DISTINCT COUNT(*) FROM policy", false);
        SqlStatement selectStatement = new SqlStatement(POLICY_SELECT_SQL, true, new SqlParameter<>("search_text", searchTextProcessed));

        statements.add(countStatement);
        statements.add(selectStatement);

        statements.addAll(PolicyController.getSearchQuery(searchTextProcessed));

        statements.add(new SqlStatement("ORDER BY relevance DESC",
                searchSearchText && orderByRelevance));

        statements.add(new SqlStatement("ORDER BY title ASC",
                !searchSearchText || !orderByRelevance));

        SqlStatement paginationStatement = new SqlStatement("LIMIT :limit OFFSET :offset",
                true,
                new SqlParameter<>("limit", size),
                new SqlParameter<>("offset", page * size));

        // Create native queries and set parameters
        Query personPaginatedQuery = SqlQuery.generate(entityManager, statements, null, "ListItem");

        countStatement.setExecute(true);
        selectStatement.setExecute(false);
        paginationStatement.setExecute(false);
        Query personCountQuery = SqlQuery.generate(entityManager, statements, null, null);

        // Get data and return results
        List<ListItem> paginatedResults = personPaginatedQuery.getResultList();
        int totalElements = ((BigInteger)personCountQuery.getSingleResult()).intValue();
        Page<ListItem> hubPage = new Page<>(paginatedResults, totalElements, orderBy, size, page);

        return hubPage;
    }
}

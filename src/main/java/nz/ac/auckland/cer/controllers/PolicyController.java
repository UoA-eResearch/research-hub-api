package nz.ac.auckland.cer.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nz.ac.auckland.cer.model.ListItem;
import nz.ac.auckland.cer.model.Page;
import nz.ac.auckland.cer.repository.PolicyRepository;
import nz.ac.auckland.cer.sql.SqlParameter;
import nz.ac.auckland.cer.sql.SqlQuery;
import nz.ac.auckland.cer.sql.SqlStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@RestController
@Api(tags = {"Person"}, description = "Operations on person")
public class PolicyController extends AbstractSearchController {

    public static String MATCH_SQL = "MATCH (name, description) AGAINST (:search_text IN BOOLEAN MODE)";
    public static String SELECT_SQL = "SELECT DISTINCT 'policy' AS 'type', id, name AS 'title', description AS 'subtitle', 'blank' AS 'image', url AS 'url', match_sql as relevance FROM policy";

    @Autowired
    private PolicyRepository policyRepository;

    public PolicyController() {
        super(SELECT_SQL, MATCH_SQL);
    }


    public static ArrayList<SqlStatement> getSearchStatements(String searchText) {
        String searchTextProcessed = SqlQuery.preProcessSearchText(searchText);
        boolean searchSearchText = !searchTextProcessed.equals("");

        ArrayList<SqlStatement> statements = new ArrayList<>();
        statements.add(new SqlStatement("WHERE", searchSearchText));
        statements.add(new SqlStatement(MATCH_SQL, searchSearchText, new SqlParameter<>("search_text", searchTextProcessed)));
        return statements;
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/policy")
    @ApiOperation(value = "search for policies")
    public Page<ListItem> getPolicy(@RequestParam Integer page,
                                    @RequestParam Integer size,
                                    @RequestParam(required = false) String orderBy,
                                    @RequestParam(required = false) String searchText) {

        return this.getSearchResults("policy", page, size, orderBy, searchText, PolicyController.getSearchStatements(searchText));
    }
}

package nz.ac.auckland.cer.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import nz.ac.auckland.cer.model.ListItem;
import nz.ac.auckland.cer.model.Page;
import nz.ac.auckland.cer.sql.SqlParameter;
import nz.ac.auckland.cer.sql.SqlQuery;
import nz.ac.auckland.cer.sql.SqlStatement;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.MediaType;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


@RestController
@RequestMapping(produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(tags = {"Person"}, description = "Operations on person")
public class PolicyController extends AbstractSearchController {

    static String MATCH_SQL = "MATCH (name, description) AGAINST (:search_text IN BOOLEAN MODE)";
    static String SELECT_SQL = "SELECT DISTINCT 'policy' AS 'type', id, name AS 'title', description AS 'subtitle', image, url AS 'url', match_sql as relevance FROM policy";

    public PolicyController() {
        super(SELECT_SQL, MATCH_SQL);
    }

    static ArrayList<SqlStatement> getSearchStatements(String searchText) {
        boolean searchSearchText = !searchText.equals("");

        ArrayList<SqlStatement> statements = new ArrayList<>();
        statements.add(new SqlStatement("WHERE", searchSearchText));
        statements.add(new SqlStatement(MATCH_SQL, searchSearchText, new SqlParameter<>("search_text", searchText)));
        return statements;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/policy")
    @ApiOperation(value = "search for policies")
    public Page<ListItem> getPolicy(@RequestParam Integer page,
                                    @RequestParam Integer size,
                                    @RequestParam(required = false) String orderBy,
                                    @RequestParam(required = false) String searchText) throws UnsupportedEncodingException {

        String searchTextProcessed = SqlQuery.preProcessSearchText(searchText);

        return this.getSearchResults("policy", page, size, orderBy, searchTextProcessed, PolicyController.getSearchStatements(searchTextProcessed));
    }
}

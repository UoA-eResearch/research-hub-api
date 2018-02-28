package nz.ac.auckland.cer.controllers;


import nz.ac.auckland.cer.model.ListItem;
import nz.ac.auckland.cer.model.Page;
import nz.ac.auckland.cer.sql.SqlParameter;
import nz.ac.auckland.cer.sql.SqlQuery;
import nz.ac.auckland.cer.sql.SqlStatement;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSearchController {

    @PersistenceContext
    private EntityManager entityManager;

    private String selectSql;
    private String matchSql;

    public AbstractSearchController(String selectSql, String matchSql) {
        this.selectSql = selectSql;
        this.matchSql = matchSql;
    }

    static String getSelectStatement(boolean searchSearchText, String selectSql, String matchSql) {
        String replaceWith = "0.0";
        if (searchSearchText) {
            replaceWith = matchSql;
        }
        return selectSql.replaceFirst("match_sql", replaceWith);
    }

    Page<ListItem> getSearchResults(String tableName, Integer page, Integer size, String orderBy, String searchText, ArrayList<SqlStatement> searchStatements) {

        // Make sure pages greater than 0 and page sizes at least 1
        page = page < 0 ? 0 : page;
        size = size < 1 ? 1 : size;

        boolean searchSearchText = !searchText.equals("");

        boolean orderByRelevance = true;
        if(orderBy != null) {
            orderByRelevance = orderBy.equals("relevance");
        }

        ArrayList<SqlStatement> statements = new ArrayList<>();

        SqlStatement countStatement = new SqlStatement("SELECT DISTINCT COUNT(*) FROM " + tableName, false);
        SqlStatement selectStatement = new SqlStatement(AbstractSearchController.getSelectStatement(searchSearchText, this.selectSql, this.matchSql), true);
        statements.add(countStatement);
        statements.add(selectStatement);

        statements.addAll(searchStatements);

        SqlStatement orderByRelStat = new SqlStatement("ORDER BY relevance DESC", searchSearchText && orderByRelevance);
        SqlStatement orderByAlpStat = new SqlStatement("ORDER BY title ASC", !searchSearchText || !orderByRelevance);
        SqlStatement paginationStatement = new SqlStatement("LIMIT :limit OFFSET :offset",
                true,
                new SqlParameter<>("limit", size),
                new SqlParameter<>("offset", page * size));
        statements.add(orderByRelStat);
        statements.add(orderByAlpStat);
        statements.add(paginationStatement);

        // Create native queries and set parameters
        Query contentPaginatedQuery = SqlQuery.generate(entityManager, statements, null, "ListItem");

        countStatement.setExecute(true);
        selectStatement.setExecute(false);
        orderByRelStat.setExecute(false);
        orderByAlpStat.setExecute(false);
        paginationStatement.setExecute(false);
        Query contentCountQuery = SqlQuery.generate(entityManager, statements, null, null);

        // Get data and return results
        List<ListItem> paginatedResults = contentPaginatedQuery.getResultList();
        int totalElements = ((BigInteger)contentCountQuery.getSingleResult()).intValue();
        return new Page<>(paginatedResults, totalElements, orderBy, size, page);
    }
}

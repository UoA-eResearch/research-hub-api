package nz.ac.auckland.cer.sql;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;


public class SqlQuery {

    public static String preProcessSearchText(String searchTextEncoded) throws UnsupportedEncodingException {
        String processed = "";

        if (searchTextEncoded != null) {
            String decoded = URLDecoder.decode(searchTextEncoded, "UTF-8");

            String[] tokens = decoded.trim().split("\\s+(?=\\S{1})");

            for (int i = 0; i < tokens.length; i++) {
                tokens[i] = tokens[i].replaceAll("[-+><()~*\"@]", " ");
            }

            processed = "+" + String.join("+", tokens);
            processed = processed.trim();
            processed += "*";
        }

        return processed;
    }

    public static Query generate(EntityManager entityManager, ArrayList<SqlStatement> statements, Class resultClass, String resultSetMapping) {
        // Create SQL String
        StringBuilder sqlStringBuilder = new StringBuilder();

        for(SqlStatement statement: statements) {
            if(statement.isExecute()) {
                sqlStringBuilder.append(statement.getSql());
                sqlStringBuilder.append("\n");
            }
        }

        String sqlString = sqlStringBuilder.toString();

        // Create query
        Query query;

        if(resultClass != null) {
            query = entityManager.createNativeQuery(sqlString, resultClass);
        } else if (resultSetMapping != null) {
            query = entityManager.createNativeQuery(sqlString, resultSetMapping);
        } else {
            query = entityManager.createNativeQuery(sqlString);
        }

        // Set parameters
        for(SqlStatement statement: statements) {
            SqlParameter[] parameters = statement.getParameters();

            if(statement.isExecute() && parameters != null) {
                for(SqlParameter parameter: parameters) {
                    query.setParameter(parameter.getName(), parameter.getParameter());
                }
            }
        }

        return query;
    }
}

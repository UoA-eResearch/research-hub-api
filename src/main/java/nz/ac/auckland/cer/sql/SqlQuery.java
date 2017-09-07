package nz.ac.auckland.cer.sql;


import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;

public class SqlQuery {

    public static Query generate(EntityManager entityManager, ArrayList<SqlStatement> statements, Class resultClass) {
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

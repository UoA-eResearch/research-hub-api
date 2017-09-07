package nz.ac.auckland.cer.sql;


public class SqlStatement {
    private String sql;
    private boolean execute;
    private SqlParameter[] parameters;

    public SqlStatement(String sql, boolean execute) {
        this.sql = sql;
        this.execute = execute;
        this.parameters = null;
    }

    public SqlStatement(String sql, boolean execute, SqlParameter... parameters) {
        this.sql = sql;
        this.execute = execute;
        this.parameters = parameters;
    }

    public String getSql() {
        return sql;
    }

    public SqlParameter[] getParameters() {
        return parameters;
    }

    public void setExecute(boolean execute) {
        this.execute = execute;
    }

    public boolean isExecute() {
        return execute;
    }
}
package nz.ac.auckland.cer.sql;


public class SqlParameter<T> {
    private String name;
    private T parameter;

    public SqlParameter(String name, T parameter) {
        this.name = name;
        this.parameter = parameter;
    }

    public String getName() {
        return name;
    }

    public T getParameter() {
        return parameter;
    }
}
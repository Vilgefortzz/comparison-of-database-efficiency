package vilgefortzz.edu.app.database_query;

public class QueryType {

    public enum type {
        select,
        insert,
        update,
        delete;
    }

    @Override
    public String toString() {
        return "QueryType";
    }
}

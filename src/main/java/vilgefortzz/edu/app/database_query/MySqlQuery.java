package vilgefortzz.edu.app.database_query;

public class MySqlQuery extends Query {

    public MySqlQuery(String query) {
        super(query);
    }

    @Override
    public void transformToAssociativeStructure() {

    }

    @Override
    public String toString() {
        return "MySqlQuery";
    }
}

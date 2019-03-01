package vilgefortzz.edu.app.database_query;

public class AssociativeNetworkQuery extends Query {


    public AssociativeNetworkQuery(String query) {
        super(query);
    }

    @Override
    public void transformToMongoDb() {

    }

    @Override
    public void transformToAssociativeNetwork() {

    }

    @Override
    public String toString() {
        return "AssociativeNetworkQuery";
    }
}

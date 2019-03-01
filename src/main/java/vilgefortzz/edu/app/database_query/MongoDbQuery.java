package vilgefortzz.edu.app.database_query;

public class MongoDbQuery extends Query {


    public MongoDbQuery(String query) {
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
        return "MongoDbQuery";
    }
}

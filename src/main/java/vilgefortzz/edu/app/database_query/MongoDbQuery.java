package vilgefortzz.edu.app.database_query;

public class MongoDbQuery extends Query {

    public final static String findAll = "{}";

    private String collectionName;
    private String findQuery;
    private String projectionQuery;

    public MongoDbQuery(String query) {

        super(query);
        createCollectionName();
        createFindQuery();
        createProjectionQuery();
    }

    private void createCollectionName() {
        collectionName = query.split("\\.")[1];
    }

    private void createFindQuery() {

        String findQuery = query.split("find")[1];
        findQuery = findQuery.split("\\(")[1];
        findQuery = findQuery.split("\\)")[0];

        this.findQuery = findQuery.split(",", 2)[0];
    }

    private void createProjectionQuery() {

        String projectionQuery = query.split("find")[1];
        projectionQuery = projectionQuery.split("\\(")[1];
        projectionQuery = projectionQuery.split("\\)")[0];

        if (projectionQuery.equals(findAll)) {
            this.projectionQuery = findAll;
        } else {
            this.projectionQuery = projectionQuery.split(", +", 2)[1];
        }
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getFindQuery() {
        return findQuery;
    }

    public void setFindQuery(String findQuery) {
        this.findQuery = findQuery;
    }

    public String getProjectionQuery() {
        return projectionQuery;
    }

    public void setProjectionQuery(String projectionQuery) {
        this.projectionQuery = projectionQuery;
    }

    @Override
    public String toString() {
        return "MongoDbQuery";
    }
}

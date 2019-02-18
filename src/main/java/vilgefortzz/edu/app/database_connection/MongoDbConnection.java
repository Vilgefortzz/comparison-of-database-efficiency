package vilgefortzz.edu.app.database_connection;

import vilgefortzz.edu.app.database_query.Query;

public class MongoDbConnection extends Connection {

    @Override
    public boolean connect() {
        return true;
    }

    @Override
    public boolean disconnect() {
        return true;
    }

    @Override
    public void query(Query query) {

    }

    @Override
    public String toString() {
        return "MongoDbConnection";
    }
}

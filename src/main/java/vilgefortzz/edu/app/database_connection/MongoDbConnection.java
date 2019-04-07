package vilgefortzz.edu.app.database_connection;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import javafx.scene.control.TableView;
import org.bson.Document;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import vilgefortzz.edu.app.database_query.MongoDbQuery;

import java.io.IOException;

import static vilgefortzz.edu.app.database_configuration.Configuration.MONGODB_HOST;
import static vilgefortzz.edu.app.database_configuration.Configuration.MONGODB_PORT;
import static vilgefortzz.edu.app.database_query.MongoDbQuery.findAll;

public class MongoDbConnection extends Connection {

    private MongoClient mongoClient;
    private MongoClientOptions mongoClientOptions = MongoClientOptions.builder()
            .serverSelectionTimeout(1000)
            .build();

    private MongoDatabase db;
    private MongoDbQuery query;

    @Override
    public boolean connectToServer() throws IOException {

        String command1 = "service";
        String command2 = "mongod";
        String command3 = "start";
        return executeCommand(command1, command2, command3);
    }

    @Override
    public boolean disconnectFromServer() throws IOException {

        String command1 = "service";
        String command2 = "mongod";
        String command3 = "stop";
        return executeCommand(command1, command2, command3);
    }

    @Override
    public boolean connectToDatabase(String dbName) {

        if (setClient()) {

            db = mongoClient.getDatabase(dbName);
            connectedToDatabase = true;

            return db != null;
        }

        return false;
    }

    @Override
    public String showDatabases() {

        String databases = "";

        if (setClient()) {

            int counter = 1;
            for (String db : mongoClient.listDatabaseNames()) {
                databases += (counter + ") " + db + "\n");
                counter++;
            }
        }

        return databases;
    }

    public boolean createDatabase(String dbName) {
        return connectToDatabase(dbName);
    }

    @Override
    public TableView query() {

        if (connectedToDatabase) {

            DB jdb = mongoClient.getDB(db.getName());
            Jongo jongo = new Jongo(jdb);
            MongoCollection collection = jongo.getCollection(query.getCollectionName());
            MongoCursor<org.bson.Document> documents;

            long startQuery, endQuery;

            if (query.getProjectionQuery().equals(findAll)) {
                startQuery = System.currentTimeMillis();
                documents = collection.find(query.getFindQuery()).as(Document.class);
                endQuery = System.currentTimeMillis();
            } else {
                startQuery = System.currentTimeMillis();
                documents = collection.find(query.getFindQuery())
                        .projection(query.getProjectionQuery()).as(Document.class);
                endQuery = System.currentTimeMillis();
            }

            query.setTime(endQuery - startQuery);

            return resultsFormatter.prepareResultsForMongoDb(documents);
        }

        return null;
    }

    private boolean setClient() {

        try {
            mongoClient = new MongoClient(
                    new ServerAddress(MONGODB_HOST, MONGODB_PORT), mongoClientOptions);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public MongoDbQuery getQuery() {
        return query;
    }

    public void setQuery(MongoDbQuery query) {
        this.query = query;
    }

    public void clearQuery() {
        query = null;
    }

    @Override
    public String toString() {
        return "MongoDbConnection";
    }
}

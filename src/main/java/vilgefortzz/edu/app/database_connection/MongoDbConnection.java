package vilgefortzz.edu.app.database_connection;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import javafx.scene.control.TableView;

import java.io.IOException;

import static vilgefortzz.edu.app.database_configuration.Configuration.MONGODB_HOST;
import static vilgefortzz.edu.app.database_configuration.Configuration.MONGODB_PORT;

public class MongoDbConnection extends Connection {

    private MongoClient mongoClient;
    private MongoClientOptions mongoClientOptions = MongoClientOptions.builder()
            .serverSelectionTimeout(1000)
            .build();

    private MongoDatabase db;

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

    @Override
    public TableView query() {
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

    @Override
    public String toString() {
        return "MongoDbConnection";
    }
}

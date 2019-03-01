package vilgefortzz.edu.app.database_connection;

import javafx.scene.control.TableView;

import java.io.IOException;

public class MongoDbConnection extends Connection {

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
    public boolean connectToDatabase(String dbName) throws IOException, InterruptedException {
        return false;
    }

    @Override
    public String showDatabases() throws IOException, InterruptedException {
        return "";
    }

    @Override
    public TableView query() {
        return null;
    }

    @Override
    public String toString() {
        return "MongoDbConnection";
    }
}

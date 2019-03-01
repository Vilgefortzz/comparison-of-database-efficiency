package vilgefortzz.edu.app.database_connection;

import javafx.scene.control.TableView;

import java.io.IOException;

public class AssociativeNetworkConnection extends Connection {


    @Override
    public boolean connectToServer() {
        return true;
    }

    @Override
    public boolean disconnectFromServer() {
        return true;
    }

    @Override
    public boolean connectToDatabase(String dbName) throws IOException, InterruptedException {
        return true;
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
        return "AssociativeNetworkConnection";
    }
}

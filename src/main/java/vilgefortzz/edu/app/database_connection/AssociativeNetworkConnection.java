package vilgefortzz.edu.app.database_connection;

import javafx.scene.control.TableView;
import vilgefortzz.edu.app.database_query.AssociativeNetworkQuery;

import java.io.IOException;

public class AssociativeNetworkConnection extends Connection {

    private AssociativeNetworkQuery query;

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

    public AssociativeNetworkQuery getQuery() {
        return query;
    }

    public void setQuery(AssociativeNetworkQuery query) {
        this.query = query;
    }

    public void clearQuery() {
        query = null;
    }

    @Override
    public String toString() {
        return "AssociativeNetworkConnection";
    }
}

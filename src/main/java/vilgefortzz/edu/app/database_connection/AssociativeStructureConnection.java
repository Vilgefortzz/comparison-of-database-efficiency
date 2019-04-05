package vilgefortzz.edu.app.database_connection;

import javafx.scene.control.TableView;
import vilgefortzz.edu.app.database_query.AssociativeStructureQuery;

import java.io.IOException;

public class AssociativeStructureConnection extends Connection {

    private AssociativeStructureQuery query;

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

    public AssociativeStructureQuery getQuery() {
        return query;
    }

    public void setQuery(AssociativeStructureQuery query) {
        this.query = query;
    }

    public void clearQuery() {
        query = null;
    }

    @Override
    public String toString() {
        return "AssociativeStructureConnection";
    }
}

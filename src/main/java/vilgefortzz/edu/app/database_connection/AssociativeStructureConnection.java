package vilgefortzz.edu.app.database_connection;

import javafx.scene.control.TableView;
import vilgefortzz.edu.app.associative_structure.Agds;
import vilgefortzz.edu.app.database_query.AssociativeStructureQuery;

import java.io.IOException;
import java.sql.SQLException;

public class AssociativeStructureConnection extends Connection {

    private AssociativeStructureQuery query;
    private Agds agds;

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

    public void generateAgds(MySqlConnection mysql) throws SQLException {
        agds = new Agds(mysql);
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

    public Agds getAgds() {
        return agds;
    }

    public void setAgds(Agds agds) {
        this.agds = agds;
    }

    @Override
    public String toString() {
        return "AssociativeStructureConnection";
    }
}

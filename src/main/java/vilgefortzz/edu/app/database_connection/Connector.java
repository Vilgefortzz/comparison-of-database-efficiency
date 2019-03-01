package vilgefortzz.edu.app.database_connection;

import javafx.scene.control.TableView;

import java.io.IOException;
import java.sql.SQLException;

public interface Connector {

    boolean connectToServer() throws IOException, InterruptedException, SQLException;
    boolean disconnectFromServer() throws IOException, InterruptedException;

    boolean connectToDatabase(String dbName) throws IOException, InterruptedException, SQLException;

    String showDatabases() throws IOException, InterruptedException, SQLException;

    TableView query() throws SQLException, IOException;
}

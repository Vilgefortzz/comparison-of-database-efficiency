package vilgefortzz.edu.app.database_connection;

import vilgefortzz.edu.app.database_query.Query;

import java.io.IOException;
import java.sql.SQLException;

public interface Connector {

    boolean connectToServer() throws IOException, InterruptedException, SQLException;
    boolean disconnectFromServer() throws IOException, InterruptedException;

    boolean connectToDatabase(String dbName) throws IOException, InterruptedException, SQLException;

    String showDatabases() throws IOException, InterruptedException, SQLException;

    void query(Query query);
}

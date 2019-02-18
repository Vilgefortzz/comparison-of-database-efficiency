package vilgefortzz.edu.app.database_connection;

import vilgefortzz.edu.app.database_query.Query;

import java.io.IOException;

public interface Connector {

    public boolean connect() throws IOException, InterruptedException;
    public boolean disconnect() throws IOException, InterruptedException;
    public void query(Query query);
}

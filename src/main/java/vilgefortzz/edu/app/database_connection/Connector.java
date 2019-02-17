package vilgefortzz.edu.app.database_connection;

import vilgefortzz.edu.app.database_query.Query;

public interface Connector {

    public void connect();
    public void disconnect();
    public void query(Query query);
}

package vilgefortzz.edu.app.database_connection;

import vilgefortzz.edu.app.database_query.Query;

public abstract class Connection {

    private Query query;
    private boolean isConnected;

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public void clearQuery() {
        query = null;
    }

    @Override
    public String toString() {
        return "Connection";
    }
}

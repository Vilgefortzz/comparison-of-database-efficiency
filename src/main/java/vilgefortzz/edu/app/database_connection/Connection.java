package vilgefortzz.edu.app.database_connection;

import vilgefortzz.edu.app.database_query.Query;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class Connection implements Connector {

    private Query query;
    private boolean isConnected;

    private ProcessBuilder builder = new ProcessBuilder();

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

    protected boolean executeCommand(String... commands) throws IOException, InterruptedException {

        builder.command(commands);
        builder.redirectErrorStream(true);

        Process process = builder.start();

        InputStream shellInfo = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(shellInfo));

        String line = null;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        return reader.readLine() == null;
    }

    @Override
    public String toString() {
        return "Connection";
    }
}

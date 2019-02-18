package vilgefortzz.edu.app.database_connection;

import vilgefortzz.edu.app.database_query.Query;

import java.io.IOException;

public class MySqlConnection extends Connection {

    @Override
    public boolean connect() throws IOException, InterruptedException {

        String command1 = "service";
        String command2 = "mysql";
        String command3 = "start";
        return executeCommand(command1, command2, command3);
    }

    @Override
    public boolean disconnect() throws IOException, InterruptedException {

        String command1 = "service";
        String command2 = "mysql";
        String command3 = "stop";
        return executeCommand(command1, command2, command3);
    }

    @Override
    public void query(Query query) {

    }

    @Override
    public String toString() {
        return "MySqlConnection";
    }
}

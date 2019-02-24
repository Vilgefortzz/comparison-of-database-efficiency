package vilgefortzz.edu.app.database_manager;

import vilgefortzz.edu.app.database_connection.MySqlConnection;

import java.io.IOException;
import java.sql.SQLException;

import static vilgefortzz.edu.app.database_configuration.Configuration.*;

public class ImportManager {

    private final String DATABASES_DIR_PATH = "./databases/";

    public boolean importToMysql(MySqlConnection connection, String dbFile, String dbName) throws IOException, SQLException {

        if (createMysqlDatabase(connection, dbName)) {
            String command1 = "sh";
            String command2 = "-c";
            String command3 = "mysql" + " " +
                    "-h" + MYSQL_HOST + " " +
                    "-u" + MYSQL_USER + " " +
                    "-p" + MYSQL_PASSWORD + " " +
                    dbName + " " + "<" + " " + DATABASES_DIR_PATH + dbFile;
            return connection.executeCommand(command1, command2, command3);
        }
        return false;
    }

    private boolean createMysqlDatabase(MySqlConnection connection, String dbName) throws SQLException {
        return connection.createDatabase(dbName);
    }

    @Override
    public String toString() {
        return "ImportManager";
    }
}

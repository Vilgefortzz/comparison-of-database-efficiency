package vilgefortzz.edu.app.database_connection;

import javafx.scene.control.TableView;

import java.io.IOException;
import java.sql.*;

import static vilgefortzz.edu.app.database_configuration.Configuration.*;

public class MySqlConnection extends Connection {

    private String url = "jdbc:mysql://" + MYSQL_HOST + ":" + MYSQL_PORT + "/";
    private final String useSSL = "?useSSL=false";

    private java.sql.Connection jdbcConnection;
    private Statement statement;

    @Override
    public boolean connectToServer() throws IOException, SQLException {

        String command1 = "service";
        String command2 = "mysql";
        String command3 = "start";
        executeCommand(command1, command2, command3);

        jdbcConnection = DriverManager.getConnection(url + useSSL, MYSQL_USER, MYSQL_PASSWORD);
        statement = jdbcConnection.createStatement();

        return statement != null;
    }

    @Override
    public boolean disconnectFromServer() throws IOException {

        String command1 = "service";
        String command2 = "mysql";
        String command3 = "stop";

        return executeCommand(command1, command2, command3);
    }

    @Override
    public boolean connectToDatabase(String dbName) throws SQLException {

        jdbcConnection = DriverManager.getConnection(url + dbName + useSSL, MYSQL_USER, MYSQL_PASSWORD);
        statement = jdbcConnection.createStatement();
        this.dbName = dbName;
        connectedToDatabase = true;

        return jdbcConnection != null;
    }

    @Override
    public String showDatabases() throws SQLException {

        jdbcConnection = DriverManager.getConnection(url + useSSL, MYSQL_USER, MYSQL_PASSWORD);
        statement = jdbcConnection.createStatement();
        String databases = "";
        int counter = 1;
        DatabaseMetaData meta = jdbcConnection.getMetaData();
        ResultSet resultSet = meta.getCatalogs();
        while (resultSet.next()) {
            String db = resultSet.getString("TABLE_CAT");
            databases += (counter + ") " + db + "\n");
            counter++;
        }

        return databases;
    }

    public boolean createDatabase(String dbName) throws SQLException {

        jdbcConnection = DriverManager.getConnection(url + useSSL, MYSQL_USER, MYSQL_PASSWORD);
        statement = jdbcConnection.createStatement();
        String query = "CREATE DATABASE IF NOT EXISTS " + dbName + ";";
        return statement.executeUpdate(query) > 0;
    }

    @Override
    public TableView query() throws SQLException, IOException {

        if (connectedToDatabase) {
            jdbcConnection = DriverManager.getConnection(url + dbName + useSSL, MYSQL_USER, MYSQL_PASSWORD);
            statement = jdbcConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(query.getQuery());

            return resultsFormatter.prepareResultsForTable(resultSet);
        }

        return null;
    }

    @Override
    public String toString() {
        return "MySqlConnection";
    }
}

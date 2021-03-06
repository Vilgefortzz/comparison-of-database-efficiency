package vilgefortzz.edu.app.database_connection;

import javafx.scene.control.TableView;
import vilgefortzz.edu.app.database_query.MySqlQuery;
import vilgefortzz.edu.app.database_results.Record;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static vilgefortzz.edu.app.database_configuration.Configuration.*;

public class MySqlConnection extends Connection {

    private String url = "jdbc:mysql://" + MYSQL_HOST + ":" + MYSQL_PORT + "/";
    private final String useSSL = "?useSSL=false";

    private java.sql.Connection jdbcConnection;
    private Statement statement;

    private String dbName;
    private MySqlQuery query;

    @Override
    public boolean connectToServer() throws IOException, SQLException {

        String command1 = "service";
        String command2 = "mysql";
        String command3 = "start";
        return executeCommand(command1, command2, command3);
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

    public List<String> getTables(String dbName) throws SQLException {

        jdbcConnection = DriverManager.getConnection(url + dbName + useSSL, MYSQL_USER, MYSQL_PASSWORD);
        statement = jdbcConnection.createStatement();

        List<String> tables = new ArrayList<>();

        DatabaseMetaData meta = jdbcConnection.getMetaData();
        ResultSet resultSet = meta.getTables(null, null,
                null, new String[] { "TABLE" });
        while (resultSet.next()) {
            tables.add(resultSet.getString("TABLE_NAME"));
        }

        return tables;
    }

    public List<String> getColumns(String dbName, String tableName) throws SQLException {

        jdbcConnection = DriverManager.getConnection(url + dbName + useSSL, MYSQL_USER, MYSQL_PASSWORD);
        statement = jdbcConnection.createStatement();

        String query = "SELECT * FROM " + tableName + ";";
        ResultSet resultSet = statement.executeQuery(query);

        List<String> columns = new ArrayList<>();

        ResultSetMetaData metaData = resultSet.getMetaData();
        int numberOfColumns = metaData.getColumnCount();

        for (int i = 0; i < numberOfColumns; i++) {
            columns.add(metaData.getColumnLabel(i+1));
        }

        return columns;
    }

    public List<Record> getRecords(String dbName, String tableName) throws SQLException {

        jdbcConnection = DriverManager.getConnection(url + dbName + useSSL, MYSQL_USER, MYSQL_PASSWORD);
        statement = jdbcConnection.createStatement();

        String query = "SELECT * FROM " + tableName + ";";
        ResultSet resultSet = statement.executeQuery(query);

        ResultSetMetaData metaData = resultSet.getMetaData();
        int numberOfColumns = metaData.getColumnCount();

        List<Record> records = new ArrayList<>();
        resultSet.next();

        do {
            Record record = new Record();
            for (int i = 1; i <= numberOfColumns; i++) {
                record.addColumnValue(metaData.getColumnLabel(i), String.valueOf(resultSet.getObject(i)));
            }
            records.add(record);
        } while (resultSet.next());

        return records;
    }

    @Override
    public TableView query() throws SQLException, IOException {

        if (connectedToDatabase) {
            jdbcConnection = DriverManager.getConnection(url + dbName + useSSL, MYSQL_USER, MYSQL_PASSWORD);
            statement = jdbcConnection.createStatement();

            long startQuery = System.currentTimeMillis();
            ResultSet resultSet = statement.executeQuery(query.getQuery());
            long endQuery = System.currentTimeMillis();

            query.setTime(endQuery - startQuery);

            return resultsFormatter.prepareResultsForMySql(resultSet);
        }

        return null;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public MySqlQuery getQuery() {
        return query;
    }

    public void setQuery(MySqlQuery query) {
        this.query = query;
    }

    public void clearQuery() {
        query = null;
    }

    @Override
    public String toString() {
        return "MySqlConnection";
    }
}

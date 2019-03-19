package vilgefortzz.edu.app.database_manager;

import vilgefortzz.edu.app.database_connection.MongoDbConnection;
import vilgefortzz.edu.app.database_connection.MySqlConnection;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static vilgefortzz.edu.app.database_configuration.Configuration.*;

public class ImportManager {

    private final String DATABASES_DIR_PATH = "./databases/";
    private final String CSV_DIR_PATH = "./databases/csv/";

    public boolean importDatabase(MySqlConnection mysql, MongoDbConnection mongodb,
                                  String dbFile, String dbName) throws IOException, SQLException {

        if (importToMysql(mysql, dbFile, dbName)) {
            return importToMongodb(mysql, mongodb, dbName);
        }

        return false;
    }

    private boolean importToMysql(MySqlConnection connection, String dbFile, String dbName) throws IOException, SQLException {

        if (connection.createDatabase(dbName)) {
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

    private boolean importToMongodb(MySqlConnection mysql, MongoDbConnection mongodb,
                                    String dbName) throws IOException, SQLException {

        if (mongodb.createDatabase(dbName)) {

            List<String> tables = mysql.getTables(dbName);

            for (String tableName : tables) {

                List<String> columns = mysql.getColumns(dbName, tableName);
                String concatColumns = prepareConcatStatement(columns);

                String query = "SELECT CONCAT(" + concatColumns + ") " +
                        "AS '' FROM " + tableName + ";";

                String command1 = "sh";
                String command2 = "-c";
                String command3 = "mysql" + " " +
                        "-h" + MYSQL_HOST + " " +
                        "-u" + MYSQL_USER + " " +
                        "-p" + MYSQL_PASSWORD + " " +
                        dbName + " " +
                        "-e" + " \"" + query + "\" " +
                        ">" + " " + CSV_DIR_PATH + tableName + ".csv";

                mysql.executeCommand(command1, command2, command3);

                String mongoFields = prepareMongoFields(columns);

                String command4 = "sh";
                String command5 = "-c";
                String command6 = "mongoimport" + " " +
                        "-d" + " " + dbName + " " +
                        "-c" + tableName + " " +
                        "--type" + " " + "csv" + " " +
                        "--file" + " " + CSV_DIR_PATH + tableName + ".csv" + " " +
                        "--fields" + " " + mongoFields;

                mongodb.executeCommand(command4, command5, command6);
            }
        }

        return false;
    }

    private String prepareConcatStatement(List<String> columns) {

        String concatStatement = "";
        String lastColumn = columns.get(columns.size() - 1);

        for (String column: columns) {
            if (column.equals(lastColumn)) {
                concatStatement += column;
            } else {
                concatStatement += column + "," + " " + "','," + " ";
            }
        }

        return concatStatement;
    }

    private String prepareMongoFields(List<String> columns) {

        String fields = "";
        String lastColumn = columns.get(columns.size() - 1);

        for (String column: columns) {
            if (column.equals(lastColumn)) {
                fields += column;
            } else {
                fields += column + ",";
            }
        }

        return fields;
    }

    @Override
    public String toString() {
        return "ImportManager";
    }
}

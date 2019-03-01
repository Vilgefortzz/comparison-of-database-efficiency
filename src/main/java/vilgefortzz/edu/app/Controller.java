package vilgefortzz.edu.app;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import vilgefortzz.edu.app.database_connection.AssociativeNetworkConnection;
import vilgefortzz.edu.app.database_connection.Connection;
import vilgefortzz.edu.app.database_connection.MongoDbConnection;
import vilgefortzz.edu.app.database_connection.MySqlConnection;
import vilgefortzz.edu.app.database_manager.ImportManager;
import vilgefortzz.edu.app.database_query.AssociativeNetworkQuery;
import vilgefortzz.edu.app.database_query.MongoDbQuery;
import vilgefortzz.edu.app.database_query.MySqlQuery;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static vilgefortzz.edu.app.database_configuration.Configuration.readConfiguration;

public class Controller implements Initializable {

    private final ToggleGroup dbConnection = new ToggleGroup();

    /**
     * Paths
     */
    private final String DATABASES_DIR_PATH = "databases/";

    /**
     * Database connection
     */
    @FXML
    private ToggleButton mysqlConnectorToggleButton;
    @FXML
    private ToggleButton mongodbConnectorToggleButton;
    @FXML
    private Circle associativeNetworkConnectorCircle;

    /**
     * Database manager
     */
    @FXML
    private Button sqlToMysqlImportButton;
    @FXML
    private Button mysqlToMongodbImportButton;
    @FXML
    private Button sqlToAssociativeNetworkGenerateButton;

    @FXML
    private Label generationTimeLabel;

    /**
     * Database query
     */
    @FXML
    private TextArea queryTextArea;
    @FXML
    private Button queryButton;
    @FXML
    private Button clearButton;

    @FXML
    private RadioButton mysqlRadioButton;
    @FXML
    private RadioButton mongodbRadioButton;
    @FXML
    private RadioButton associativeNetworkRadioButton;

    /**
     * Database chooser
     */
    @FXML
    private Label mysqlDatabasesLabel;
    @FXML
    private ScrollPane mysqlDatabasesScrollPane;
    @FXML
    private Button showMysqlDbsButton;
    @FXML
    private TextField mysqlDbNameTextField;
    @FXML
    private Button connectToMysqlDbButton;

    @FXML
    private Label mongoDatabasesLabel;
    @FXML
    private ScrollPane mongoDatabasesScrollPane;
    @FXML
    private Button showMongoDbsButton;
    @FXML
    private TextField mongoDbNameTextField;
    @FXML
    private Button connectToMongoDbButton;

    /**
     * Results
     */
    @FXML
    private Label queryResultsLabel;
    @FXML
    private ScrollPane queryResultsScrollPane;

    @FXML
    private Label queryTimeLabel;

    private Map<String, Connection> connections = new HashMap<>();

    private ImportManager importManager = new ImportManager();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        readConfiguration();
        initializeRadioButtons();
        initializeConnections();
    }

    @FXML
    public void connectToMysqlServer() throws IOException, InterruptedException, SQLException {

        Connection mysql = connections.get("mysql");
        connectToServer(mysql, mysqlConnectorToggleButton);
    }

    @FXML
    public void connectToMongodbServer() throws IOException, InterruptedException, SQLException {

        Connection mongodb = connections.get("mongodb");
        connectToServer(mongodb, mongodbConnectorToggleButton);
    }

    @FXML
    public void connectToMysqlDatabase() throws IOException, InterruptedException, SQLException {

        String dbName = mysqlDbNameTextField.getText();
        Connection mysql = connections.get("mysql");
        mysql.connectToDatabase(dbName);
    }

    @FXML
    public void connectToMongoDatabase() throws IOException, InterruptedException, SQLException {

        String dbName = mongoDbNameTextField.getText();
        Connection mongodb = connections.get("mongodb");
        mongodb.connectToDatabase(dbName);
    }

    @FXML
    public void showMysqlDatabases() throws IOException, InterruptedException, SQLException {

        mysqlDbNameTextField.setDisable(false);
        connectToMysqlDbButton.setDisable(false);

        Connection mysql = connections.get("mysql");
        String databases = mysql.showDatabases();
        mysqlDatabasesLabel.setText(databases);
    }

    @FXML
    public void showMongoDatabases() throws IOException, InterruptedException {

        mongoDbNameTextField.setDisable(false);
        connectToMongoDbButton.setDisable(false);
    }

    @FXML
    public void importFromSqlToMysql() throws IOException, SQLException {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Import from SQL to MySQL");
        chooser.setInitialDirectory(new File(DATABASES_DIR_PATH));
        chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("sql files","sql"));
        File file = chooser.showOpenDialog(new Stage());

        if (file != null) {
            String dbFile = file.getName();
            String extension = dbFile.split("\\.")[1];
            if (extension.equals("sql")) {
                String dbName = dbFile.split("\\.")[0].
                        replaceAll("[^a-zA-Z0-9]", "");
                MySqlConnection mysql = (MySqlConnection) connections.get("mysql");
                importManager.importToMysql(mysql, dbFile, dbName);
            } else {
                System.out.println("You can only import sql files!");
            }
        }
    }

    @FXML
    public void query() throws SQLException, IOException {

        String query = queryTextArea.getText();

        if (mysqlRadioButton.isSelected()) {

            Connection mysql = connections.get("mysql");
            mysql.setQuery(new MySqlQuery(query));

            TableView results = mysql.query();

            queryResultsScrollPane.setFitToWidth(true);
            queryResultsScrollPane.setFitToHeight(true);
            queryResultsScrollPane.setContent(results);

        } else if (mongodbRadioButton.isSelected()) {

            Connection mongodb = connections.get("mongodb");
            mongodb.setQuery(new MongoDbQuery(query));
            mongodb.getQuery().transformToMongoDb();
        } else {

            Connection associativeNetwork = connections.get("associativeNetwork");
            associativeNetwork.setQuery(new AssociativeNetworkQuery(query));
            associativeNetwork.getQuery().transformToAssociativeNetwork();
        }
    }

    @FXML
    public void clearQuery() {
        queryTextArea.clear();
    }

    private void initializeRadioButtons() {

        mysqlRadioButton.setToggleGroup(dbConnection);
        mysqlRadioButton.setSelected(true);

        mongodbRadioButton.setToggleGroup(dbConnection);
        associativeNetworkRadioButton.setToggleGroup(dbConnection);
    }

    private void initializeConnections() {

        connections.put("mysql", new MySqlConnection());
        connections.put("mongodb", new MongoDbConnection());
        connections.put("associativeNetwork", new AssociativeNetworkConnection());
    }

    private void connectToServer(Connection connection, ToggleButton connectionToggleButton) throws IOException, InterruptedException, SQLException {

        if (connectionToggleButton.isSelected()) {
            if (!connection.isConnectedToServer() && connection.connectToServer()) {
                connectionToggleButton.setText("Off");
                connectionToggleButton.setTextFill(Color.web("#e1400a"));
                connection.setConnectedToServer(true);
            }
            return;
        }

        if (connection.isConnectedToServer() && connection.disconnectFromServer()) {
            connectionToggleButton.setText("On");
            connectionToggleButton.setTextFill(Color.web("#08d70b"));
            connection.setConnectedToServer(false);
        }
    }
}

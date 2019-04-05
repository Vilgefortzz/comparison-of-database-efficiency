package vilgefortzz.edu.app;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import vilgefortzz.edu.app.database_connection.AssociativeStructureConnection;
import vilgefortzz.edu.app.database_connection.Connection;
import vilgefortzz.edu.app.database_connection.MongoDbConnection;
import vilgefortzz.edu.app.database_connection.MySqlConnection;
import vilgefortzz.edu.app.database_manager.ImportManager;
import vilgefortzz.edu.app.database_query.AssociativeStructureQuery;
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
    private Circle associativeStructureConnectorCircle;

    /**
     * Database manager
     */
    @FXML
    private Button importDatabaseButton;
    @FXML
    private Button mysqlToAssociativeStructureGenerateButton;

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
    private RadioButton associativeStructureRadioButton;

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
        String mysqlDatabases = mysql.showDatabases();

        Text databases = new Text(mysqlDatabases);

        mysqlDatabasesScrollPane.setFitToWidth(true);
        mysqlDatabasesScrollPane.setFitToHeight(true);
        mysqlDatabasesScrollPane.setContent(databases);
    }

    @FXML
    public void showMongoDatabases() throws IOException, InterruptedException, SQLException {

        mongoDbNameTextField.setDisable(false);
        connectToMongoDbButton.setDisable(false);

        Connection mongodb = connections.get("mongodb");
        String mongoDatabases = mongodb.showDatabases();

        Text databases = new Text(mongoDatabases);

        mongoDatabasesScrollPane.setFitToWidth(true);
        mongoDatabasesScrollPane.setFitToHeight(true);
        mongoDatabasesScrollPane.setContent(databases);
    }

    @FXML
    public void importDatabase() throws IOException, SQLException {

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
                MongoDbConnection mongodb = (MongoDbConnection) connections.get("mongodb");
                if (importManager.importDatabase(mysql, mongodb, dbFile, dbName)) {
                    System.out.println("Database successfully imported!");
                }
            } else {
                System.out.println("You can only import sql files!");
            }
        }
    }

    @FXML
    public void generateAgds() throws IOException, SQLException {


    }

    @FXML
    public void query() throws SQLException, IOException {

        String query = queryTextArea.getText();
        TableView results = null;

        if (mysqlRadioButton.isSelected()) {

            MySqlConnection mysql = (MySqlConnection) connections.get("mysql");
            mysql.setQuery(new MySqlQuery(query));

            results = mysql.query();

            // Set query time
            long queryTimeInNs = mysql.getQuery().getTime();
            long queryTimeInMs = queryTimeInNs / 1000;
            queryTimeLabel.setText(queryTimeInMs + " ms\n" + queryTimeInNs + " ns");

        } else if (mongodbRadioButton.isSelected()) {

            MongoDbConnection mongodb = (MongoDbConnection) connections.get("mongodb");
            mongodb.setQuery(new MongoDbQuery(query));

            results = mongodb.query();

            // Set query time
            long queryTimeInNs = mongodb.getQuery().getTime();
            long queryTimeInMs = queryTimeInNs / 1000;
            queryTimeLabel.setText(queryTimeInMs + " ms\n" + queryTimeInNs + " ns");

        } else {

            AssociativeStructureConnection associativeStructure = (AssociativeStructureConnection) connections.get("associativeStructure");
            associativeStructure.setQuery(new AssociativeStructureQuery(query));
            associativeStructure.getQuery().transformToAssociativeStructure();
        }

        // Set results
        queryResultsScrollPane.setFitToWidth(true);
        queryResultsScrollPane.setFitToHeight(true);
        queryResultsScrollPane.setContent(results);
    }

    @FXML
    public void clearQuery() {
        queryTextArea.clear();
    }

    private void initializeRadioButtons() {

        mysqlRadioButton.setToggleGroup(dbConnection);
        mysqlRadioButton.setSelected(true);

        mongodbRadioButton.setToggleGroup(dbConnection);
        associativeStructureRadioButton.setToggleGroup(dbConnection);
    }

    private void initializeConnections() {

        connections.put("mysql", new MySqlConnection());
        connections.put("mongodb", new MongoDbConnection());
        connections.put("associativeStructure", new AssociativeStructureConnection());
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

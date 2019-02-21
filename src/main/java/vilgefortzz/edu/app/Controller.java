package vilgefortzz.edu.app;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import vilgefortzz.edu.app.database_connection.AssociativeNetworkConnection;
import vilgefortzz.edu.app.database_connection.Connection;
import vilgefortzz.edu.app.database_connection.MongoDbConnection;
import vilgefortzz.edu.app.database_connection.MySqlConnection;
import vilgefortzz.edu.app.database_query.AssociativeNetworkQuery;
import vilgefortzz.edu.app.database_query.MongoDbQuery;
import vilgefortzz.edu.app.database_query.MySqlQuery;
import vilgefortzz.edu.app.database_query.Query;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private final ToggleGroup dbConnection = new ToggleGroup();

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
     * Results
     */
    @FXML
    private Label queryResultsLabel;
    @FXML
    private Label queryTimeLabel;

    private Map<String, Connection> connections = new HashMap<>();
    private Map<String, Query> queries = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeRadioButtons();
        initializeConnections();
        initializeQueries();
    }

    @FXML
    public void connectToMysql() throws IOException, InterruptedException {

        Connection mysql = connections.get("mysql");
        connect(mysql, mysqlConnectorToggleButton);
    }

    @FXML
    public void connectToMongodb() throws IOException, InterruptedException {

        Connection mongodb = connections.get("mongodb");
        connect(mongodb, mongodbConnectorToggleButton);
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

    private void initializeQueries() {

        queries.put("mysql", new MySqlQuery());
        queries.put("mongodb", new MongoDbQuery());
        queries.put("associativeNetwork", new AssociativeNetworkQuery());
    }

    private void connect(Connection connection, ToggleButton connectionToggleButton) throws IOException, InterruptedException {

        if (connectionToggleButton.isSelected()) {
            if (!connection.isConnectedToServer() && connection.connect()) {
                connectionToggleButton.setText("Off");
                connectionToggleButton.setTextFill(Color.web("#e1400a"));
                connection.setConnectedToServer(true);
            }
            return;
        }

        if (connection.isConnectedToServer() && connection.disconnect()) {
            connectionToggleButton.setText("On");
            connectionToggleButton.setTextFill(Color.web("#08d70b"));
            connection.setConnectedToServer(false);
        }
    }
}

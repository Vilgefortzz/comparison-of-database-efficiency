package vilgefortzz.edu.app;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private final ToggleGroup dbConnection = new ToggleGroup();

    /**
     * Database connector
     */
    @FXML
    private ImageView mysqlConnectorImageView;
    @FXML
    private ToggleButton mysqlConnectorToggleButton;
    @FXML
    private ImageView mongodbConnectorImageView;
    @FXML
    private ToggleButton mongodbConnectorToggleButton;

    /**
     * Database manager
     */
    @FXML
    private ImageView sql1ImageView;
    @FXML
    private ImageView mysql1ImageView;
    @FXML
    private Button sqlToMysqlImportButton;
    @FXML
    private ImageView mysql2ImageView;
    @FXML
    private ImageView mongodbImageView;
    @FXML
    private Button mysqlToMongodbImportButton;
    @FXML
    private ImageView sql2ImageView;
    @FXML
    private ImageView associativeNetworkImageView;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeRadioButtons();
    }

    private void initializeRadioButtons() {

        mysqlRadioButton.setToggleGroup(dbConnection);
        mysqlRadioButton.setSelected(true);

        mongodbRadioButton.setToggleGroup(dbConnection);
        associativeNetworkRadioButton.setToggleGroup(dbConnection);
    }
}

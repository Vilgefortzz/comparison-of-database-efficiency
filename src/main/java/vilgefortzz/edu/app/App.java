package vilgefortzz.edu.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class App extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        URL layout = getClass().getClassLoader().getResource("scene.fxml");
        if (layout == null) { throw new Exception("Cannot load a scene layout"); }

        Parent root = FXMLLoader.load(layout);

        primaryStage.setTitle("Comparison of database efficiency");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}

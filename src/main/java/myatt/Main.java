package myatt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import myatt.helper.DBConnection;


import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;


/**
 * The main class launches the scheduling application.
 * This is the entry point to the JavaFX application.
 * It handles the initialize and launching the application
 */
public class Main extends Application {

    /**
     * Starts the application by loading the LoginScreen.fxml file.
     * Applies the Language resource bundle.
     * Sets up the main stage for the application.
     *
     * @param primaryStage the primary stage for the application.
     * @throws IOException if the FXML file cannot be loaded.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginScreen.fxml"));
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Language", Locale.getDefault());
        loader.setResources(resourceBundle);
        Parent root = loader.load();
        primaryStage.setTitle("Scheduling Application");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * The main method establishes the database connection and launches the application.
     *
     * @param args command line argument for launching the application.
     */
    public static void main(String[] args)  {

        DBConnection.openConnection();
        launch(args);
    }

}
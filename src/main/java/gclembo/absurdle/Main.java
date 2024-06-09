package gclembo.absurdle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * This application runs a game of Absurdle.
 */
public class Main extends Application {

    /**
     * Runs Absurdle application.
     * @param stage Stage for application display.
     */
    @Override
    public void start(Stage stage) throws IOException {
        // Loads FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // Sets css styling
        String css = this.getClass().getResource("main.css").toExternalForm();
        scene.getStylesheets().add(css);

        // Sets window properties
        stage.setTitle("Absurdle");
        stage.getIcons().add(new Image("absurdle.png"));

        // Displays window
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}
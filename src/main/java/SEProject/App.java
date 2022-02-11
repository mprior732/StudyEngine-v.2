package SEProject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {


    //main function to launch Study Engine
    public static void main(String[] args) {
        launch();
    }

    //starts GUI
    @Override
    public void start(Stage stage) throws IOException {

        //set login page dimensions
        Scene scene = new Scene(loadFXML(), 900, 600);

        //set stage title and create window
        stage.setTitle("Study Engine");
        stage.setScene(scene);
        stage.show();
    }

    //function to return login fxml file
    private static Parent loadFXML() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("loginPage.fxml"));
        return fxmlLoader.load();
    }

}
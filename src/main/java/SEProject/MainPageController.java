package SEProject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class MainPageController {

    @FXML
    public Label displayName;
    @FXML
    public Button logoutButton;
    @FXML
    public Button testButton;
    @FXML
    public Button studyButton;
    @FXML
    public Button editButton;


    //set users name to the welcome page
    public void setName(String name){
        displayName.setText(name);
    }


    //logout and redirect to login page
    public void handleLogout(ActionEvent event) throws IOException{

        FXMLLoader loader = new FXMLLoader(App.class.getResource("loginPage.fxml"));
        Stage stage = (Stage)logoutButton.getScene().getWindow();
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void handleTest(ActionEvent event) throws IOException, SQLException {

        User user = User.getByUsername(GlobalID.getUserIdentifier());
        User thisUser = new User(user.getID(), user.getFirstName(), user.getLastName(), user.getUsername());

        FXMLLoader loader = new FXMLLoader(App.class.getResource("testPage.fxml"));
        Stage stage = (Stage)testButton.getScene().getWindow();
        Parent root = loader.load();

        //populate combo box on page load
        TestPageController testPage = loader.getController();
        testPage.setCourseList(thisUser.getID());

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void handleStudy(ActionEvent event) throws IOException, SQLException {


        User user = User.getByUsername(GlobalID.getUserIdentifier());
        User thisUser = new User(user.getID(), user.getFirstName(), user.getLastName(), user.getUsername());

        FXMLLoader loader = new FXMLLoader(App.class.getResource("studyPage.fxml"));
        Stage stage = (Stage)studyButton.getScene().getWindow();
        Parent root = loader.load();

        //populate combo box on page load
        StudyPageController studyPage = loader.getController();
        studyPage.setCourseList(thisUser.getID());

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void handleEdit(ActionEvent event) throws IOException, SQLException {

        User user = User.getByUsername(GlobalID.getUserIdentifier());
        User thisUser = new User(user.getID(), user.getFirstName(), user.getLastName(), user.getUsername());

        FXMLLoader loader = new FXMLLoader(App.class.getResource("editPage.fxml"));
        Stage stage = (Stage)editButton.getScene().getWindow();
        Parent root = loader.load();

        //populate combo box on page load
        EditPageController editPage = loader.getController();
        editPage.setCourseList(thisUser.getID());

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
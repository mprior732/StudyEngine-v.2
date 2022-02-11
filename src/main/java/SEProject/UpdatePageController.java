package SEProject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class UpdatePageController {

    //update page elements
    @FXML
    public Label courseToEdit;
    @FXML
    public Label oldQuestion;
    @FXML
    public TextArea updateQuestion;
    @FXML
    public TextArea updateAnswer;
    @FXML
    public Button confirmUpdate;
    @FXML
    public Button cancelUpdate;


    public void setCourseItems(String course, String question, String answer){

        courseToEdit.setText(course);
        updateQuestion.setText(question);
        updateAnswer.setText(answer);

        oldQuestion.setText(question);
    }


    public void updateQuestion(ActionEvent event) throws SQLException, IOException {
        String course, question, newQuestion, newAnswer;
        course = courseToEdit.getText();
        question = oldQuestion.getText();
        newQuestion = updateQuestion.getText();
        newAnswer = updateAnswer.getText();

        Courses c = new Courses();


        //get current users data
        User user = User.getByUsername(GlobalID.getUserIdentifier());
        User thisUser = new User(user.getID(), user.getFirstName(), user.getLastName(),
                user.getUsername());

        c.updateCourse(course, question, newQuestion, newAnswer, thisUser.getID());

        FXMLLoader loader = new FXMLLoader(App.class.getResource("editPage.fxml"));
        Stage stage = (Stage)confirmUpdate.getScene().getWindow();
        Parent root = loader.load();

        //Populate the combo box on the edit page with users courses
        EditPageController editPage = loader.getController();
        editPage.setCourseList(thisUser.getID());
        editPage.setConfirmationLabel();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void handleCancel(ActionEvent event) throws SQLException, IOException {

        //get current users data
        User user = User.getByUsername(GlobalID.getUserIdentifier());
        User thisUser = new User(user.getID(), user.getFirstName(), user.getLastName(),
                user.getUsername());

        FXMLLoader loader = new FXMLLoader(App.class.getResource("editPage.fxml"));
        Stage stage = (Stage)confirmUpdate.getScene().getWindow();
        Parent root = loader.load();

        //Populate the combo box on the edit page with users courses
        EditPageController editPage = loader.getController();
        editPage.setCourseList(thisUser.getID());

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}

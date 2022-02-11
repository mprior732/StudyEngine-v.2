package SEProject;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class StudyPageController {

    @FXML
    public Label correctLabel;
    @FXML
    public Label errorLabel;
    @FXML
    public Label incorrectLabel;
    @FXML
    public Label courseErrorLabel;
    @FXML
    public CheckBox checkBoxTrue;
    @FXML
    public CheckBox checkBoxFalse;
    @FXML
    public TextArea questionField;
    @FXML
    public TextArea answerField;
    @FXML
    public ComboBox<String> courseList;
    @FXML
    public Button backButton;


    //function to add a list of the current users courses to the comboboxes
    public void setCourseList(int userID){

        Courses courses = new Courses();
        ObservableList<String> list = courses.getCourseListByUserID(userID);

        courseList.setItems(list);
    }

    //function to send user back to main page
    public void handleBack() throws SQLException, IOException {
        User user = User.getByUsername(GlobalID.getUserIdentifier());
        User thisUser = new User(user.getID(), user.getFirstName(), user.getLastName(),
                user.getUsername());

        //load main page with valid user credentials
        FXMLLoader loader = new FXMLLoader(App.class.getResource("mainPage.fxml"));
        Stage stage = (Stage)backButton.getScene().getWindow();
        Parent root = loader.load();

        //set user name to main page when loaded
        MainPageController mainPage = loader.getController();
        mainPage.setName(thisUser.getFirstName());

        //set scene
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    //function to uncheck false if true is checked
    public void uncheckFalse(ActionEvent event){
        checkBoxFalse.setSelected(false);
    }

    //function to uncheck true if false is checked
    public void uncheckTrue(ActionEvent event){
        checkBoxTrue.setSelected(false);
    }

    //function to generate a question with a correct or incorrect answer
    public void startStudying(ActionEvent event) throws SQLException {

        courseErrorLabel.setText("");
        incorrectLabel.setText("");
        correctLabel.setText("");
        errorLabel.setText("");

        checkBoxFalse.setSelected(false);
        checkBoxTrue.setSelected(false);

        User user = User.getByUsername(GlobalID.getUserIdentifier());
        User thisUser = new User(user.getID(), user.getFirstName(), user.getLastName(),
                user.getUsername());

        Courses c = new Courses();

        String course;
        int correctIndex, incorrectIndex, flip;

        course = courseList.getValue();

        ArrayList<String> qList;
        ArrayList<String> aList;

        if(course == null){
            courseErrorLabel.setText("Please select a course then try again");
        }else {

            //retrieve the question and answer lists
            qList = c.getQuestions(course, thisUser.getID());
            aList = c.getAnswers(course, thisUser.getID());

            correctIndex = new Random().nextInt(qList.size());
            incorrectIndex = new Random().nextInt(qList.size());

            //make sure the two indexes don't match
            while(correctIndex == incorrectIndex){
                incorrectIndex = new Random().nextInt(qList.size());
            }

            questionField.setText(qList.get(correctIndex));

            flip = new Random().nextInt(2);

            if(flip == 0){
                answerField.setText(aList.get(incorrectIndex));
            }else {
                answerField.setText(aList.get(correctIndex));
            }
        }
    }

    //function to check if user selected the correct answer
    public void checkAnswer(ActionEvent event) throws SQLException {

        User user = User.getByUsername(GlobalID.getUserIdentifier());
        User thisUser = new User(user.getID(), user.getFirstName(), user.getLastName(),
                user.getUsername());

        Courses c = new Courses();

        String course, question, answer;

        course = courseList.getValue();
        question = questionField.getText();
        answer = answerField.getText();

        if(!c.questionExists(question, course, thisUser.getID())) {//make sure user didn't change course
            courseErrorLabel.setText("Make sure you have the right course selected and try again");
        }else if(!checkBoxTrue.isSelected() && !checkBoxFalse.isSelected()){//make sure user selected and answer
            courseErrorLabel.setText("");
            incorrectLabel.setText("");
            correctLabel.setText("");
            errorLabel.setText("Please select true or false");
        }else if(c.isCorrectAnswer(course, question, answer, thisUser.getID()) && checkBoxTrue.isSelected()){
            courseErrorLabel.setText("");
            incorrectLabel.setText("");
            errorLabel.setText("");
            correctLabel.setText("Correct!");
        }else if(!c.isCorrectAnswer(course, question, answer, thisUser.getID()) && checkBoxFalse.isSelected()) {
            courseErrorLabel.setText("");
            incorrectLabel.setText("");
            errorLabel.setText("");
            correctLabel.setText("Correct!");
        }else{
            courseErrorLabel.setText("");
            correctLabel.setText("");
            errorLabel.setText("");
            incorrectLabel.setText("Incorrect!");
        }
    }

}

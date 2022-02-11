package SEProject;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.ArrayList;

public class TestPageController {

    @FXML
    public ComboBox<String> courseList;
    @FXML
    public Label courseErrorLabel;
    @FXML
    public ProgressBar progressBar;
    @FXML
    public TextArea questionField;
    @FXML
    public RadioButton answer1;
    @FXML
    public RadioButton answer2;
    @FXML
    public RadioButton answer3;
    @FXML
    public RadioButton answer4;



    //function to add a list of the current users courses to the comboboxes
    public void setCourseList(int userID){

        Courses courses = new Courses();
        ObservableList<String> list = courses.getCourseListByUserID(userID);

        courseList.setItems(list);
    }


    public void startTest(ActionEvent event) throws SQLException {

        User user = User.getByUsername(GlobalID.getUserIdentifier());
        User thisUser = new User(user.getID(), user.getFirstName(), user.getLastName(),
                user.getUsername());

        Courses c = new Courses();

        String course;
        course = courseList.getValue();

        if(course == null) {
            courseErrorLabel.setText("Please select a course and try again");
        }else{
            ArrayList<String> qList;
            ArrayList<String> aList;

            qList = c.getQuestions(course, thisUser.getID());
            aList = c.getAnswers(course, thisUser.getID());
        }
    }

    public void loadTestQuestion(ArrayList<String> qList, ArrayList<String> aList) throws SQLException {

        User user = User.getByUsername(GlobalID.getUserIdentifier());
        User thisUser = new User(user.getID(), user.getFirstName(), user.getLastName(),
                user.getUsername());

        Courses c = new Courses();
        String course;
        course = courseList.getValue();

        String question, correctAnswer;





    }

    public void displayTestQuestion(){

    }
}

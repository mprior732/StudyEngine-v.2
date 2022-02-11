package SEProject;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class EditPageController {

    @FXML
    public ComboBox<String> courseList;
    @FXML
    public ComboBox<String> editCourseList;
    @FXML
    public Button backButton;

    //add functionality elements
    @FXML
    public TextField newCourseName;
    @FXML
    public TextArea newQuestion;
    @FXML
    public TextArea newAnswer;
    @FXML
    public Button addNew;
    @FXML
    public Label errorLabel;

    //edit functionality elements
    @FXML
    public TableView<Courses> courseTable;
    @FXML
    public TableColumn<Courses, String> question_col;
    @FXML
    public TableColumn<Courses, String> answer_col;
    @FXML
    public Label errorLabel2;
    @FXML
    public Button editButton;
    @FXML
    public Label confirmationLabel;




    //declare database variables
    DBHandler db = new DBHandler();
    Connection conn = null;
    PreparedStatement st = null;

    //function to add a list of the current users courses to the comboboxes
    public void setCourseList(int userID){

        Courses courses = new Courses();
        ObservableList<String> list = courses.getCourseListByUserID(userID);

        courseList.setItems(list);
        editCourseList.setItems(list);
    }

    public void setConfirmationLabel(){

        confirmationLabel.setText("Question updated successfully!");
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


    //function to add new questions to the StudyEngine database
    public void addNewQuestion(ActionEvent event) throws SQLException {
        confirmationLabel.setText("");
        errorLabel2.setText("");

        String question, answer, newCourse, course;
        String query = "INSERT INTO courses(courseName, question, answer, userID) " +
                "VALUES(?, ?, ?, ?)";

        //get current users data
        User user = User.getByUsername(GlobalID.getUserIdentifier());
        User thisUser = new User(user.getID(), user.getFirstName(), user.getLastName(),
                user.getUsername());

        //create a course object
        Courses c = new Courses();

        //retrieve data from the text fields
        question = newQuestion.getText();
        answer = newAnswer.getText();
        newCourse = newCourseName.getText();
        course = courseList.getValue();

        //prevent user from adding an already existing course
        if(c.courseExists(newCourse, thisUser.getID())){
            errorLabel.setText("This course already exists. Try selecting it in the dropdown instead");
        }else {
            if ((newCourse.equals("") && course == null) || (!newCourse.equals("") && !(course == null))) {
                errorLabel.setText("Please pick either a new course name or an existing course and try again");
            } else if (question.equals("") || answer.equals("")) {
                errorLabel.setText("Please fill out both the question field and answer field and try again");
            } else {

                if (!newCourse.equals("")) {
                    course = newCourse;
                }

                //check if question already exists for the user
                if (c.questionExists(question, course, thisUser.getID())) {
                    errorLabel.setText("That question already exists for this course");
                } else {

                    try {
                        //connect to the database
                        conn = db.DBDriver();
                        //run query to add a new question and answer to a course
                        st = conn.prepareStatement(query);

                        st.setString(1, course);
                        st.setString(2, question);
                        st.setString(3, answer);
                        st.setInt(4, thisUser.getID());

                        st.execute();

                        //refresh combobox course list
                        setCourseList(thisUser.getID());
                        errorLabel.setText("Question added to course " + course + " successfully!");

                        //clear text fields
                        newCourseName.setText("");
                        newQuestion.setText("");
                        newAnswer.setText("");
                    } catch (SQLException e) {
                        e.printStackTrace();
                        e.getMessage();
                        errorLabel.setText("Unable to add anything at this time, try again later");
                    } finally {
                        //shutdown database and clear cache
                        db.closeDB();
                    }
                }
            }
        }
    }

    //function to retreive and display all questions of a given course
    public void displayCourseInfo(ActionEvent event) throws SQLException {
        confirmationLabel.setText("");
        errorLabel2.setText("");

        String course;
        course = editCourseList.getValue();
        ObservableList<Courses> list;
        Courses c = new Courses();

        //get current users data
        User user = User.getByUsername(GlobalID.getUserIdentifier());
        User thisUser = new User(user.getID(), user.getFirstName(), user.getLastName(),
                user.getUsername());

        if(course == null){
            errorLabel2.setText("Please select a course from the dropdown, then try again");
        }else {

            question_col.setCellValueFactory(new PropertyValueFactory<Courses, String>("question"));
            answer_col.setCellValueFactory(new PropertyValueFactory<Courses, String>("answer"));

            list = c.getCourseInfo(course, thisUser.getID());
            courseTable.setItems(list);
        }
    }

    public void deleteQuestion(ActionEvent event) throws SQLException {
        confirmationLabel.setText("");
        errorLabel2.setText("");

        String question, course;

        course = editCourseList.getValue();

        Courses tableRow = courseTable.getSelectionModel().getSelectedItem();

        //get current users data
        User user = User.getByUsername(GlobalID.getUserIdentifier());
        User thisUser = new User(user.getID(), user.getFirstName(), user.getLastName(),
                user.getUsername());


        if (course == null) {
            errorLabel2.setText("Please make sure a course is selected in the dropdown");
        } else if (tableRow == null) {
            errorLabel2.setText("Make sure a question is selected first before attempting to delete");
        } else {

            //Create Pop-Up to confirm deletion of question
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you would like to delete this question?", yes, no);

            alert.setTitle("Delete Question From " + course);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.orElse(no) == yes) {
                question = tableRow.getQuestion();

                tableRow.deleteFromCourseList(course, question, thisUser.getID());

                courseTable.getItems().remove(tableRow);
                errorLabel2.setText("");
            }

        }

    }

    public void loadUpdatePage(ActionEvent event) throws IOException {

        String course, question, answer;
        Courses tableRow = courseTable.getSelectionModel().getSelectedItem();

        course = editCourseList.getValue();

        if(course == null){
            errorLabel2.setText("Please select a course from the dropdown, then try again");
        }else if(tableRow == null){
            errorLabel2.setText("Make sure a question is selected first before attempting to delete");
        }else{
            question = tableRow.getQuestion();
            answer = tableRow.getAnswer();

            //load update page with course data
            FXMLLoader loader = new FXMLLoader(App.class.getResource("updatePage.fxml"));
            Stage stage = (Stage)editButton.getScene().getWindow();
            Parent root = loader.load();

            UpdatePageController updatePage = loader.getController();
            updatePage.setCourseItems(course, question, answer);

            //set scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }


    }
}

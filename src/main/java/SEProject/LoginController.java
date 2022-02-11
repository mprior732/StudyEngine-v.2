package SEProject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.regex.Pattern;

public class LoginController extends MainPageController {
    @FXML
    public Button readMe;

    //login elements
    @FXML
    public TextField usernameLogin;
    @FXML
    public PasswordField passwordLogin;
    @FXML
    public Button loginButton;

    //create account elements
    @FXML
    public TextField firstNameField;
    @FXML
    public TextField lastNameField;
    @FXML
    public TextField newUsername;
    @FXML
    public TextField newPassword;
    @FXML
    public TextField rePassword;
    @FXML
    public Label notificationLabel;



    DBHandler db = new DBHandler();
    Connection conn = null;
    PreparedStatement st = null;
    ResultSet rs = null;


    //function for users to create an account
    public void createNewUser(ActionEvent event) throws SQLException {
        String firstName, lastName, username, password, rePass;

        //retrieve input field data
        firstName = firstNameField.getText();
        lastName = lastNameField.getText();
        username = newUsername.getText();
        password = newPassword.getText();
        rePass = rePassword.getText();

        //set character restrictions for certain input fields
        Pattern charNamePattern = Pattern.compile("[^a-z]", Pattern.CASE_INSENSITIVE);
        Pattern charPattern = Pattern.compile("[^a-z0-9!?]", Pattern.CASE_INSENSITIVE);

        String query = "INSERT INTO users(firstName, lastName, username, password)" +
                "VALUES(?, ?, ?, ?)";

        if(!password.equals(rePass)){//check to make sure passwords match
            notificationLabel.setText("Passwords don't match");
        }else if(firstName.equals("") || lastName.equals("") || username.equals("")
                || password.equals("")){//check to make sure inputs aren't empty
            notificationLabel.setText("Make sure all fields are complete and try again");
        }else if(charNamePattern.matcher(firstName).find() || charNamePattern.matcher(lastName).find()) {
            //restrict first/last name characters to only uppercase/lowercase letters
            notificationLabel.setText("Only uppercase and lowercase letters allowed in first/last name field");
        }else if(charPattern.matcher(username).find() || charPattern.matcher(password).find()){
            //restrict most special characters from username/password
            notificationLabel.setText("Special characters SPACE ; : , <> {} [] () \' \" - + = _ ~ ` # & * $ ^ @ are not allowed");
        }else {//add input data to the database

            try {
                conn = db.DBDriver();
                st = conn.prepareStatement(query);

                st.setString(1, firstName);
                st.setString(2, lastName);
                st.setString(3, username);
                st.setString(4, password);

                st.execute();

                notificationLabel.setText("Congratulations, account created successfully!!");
            } catch (SQLException e) {
                System.out.println(e.getMessage());

                //check to see if username is unique
                if(e.getMessage().contains("duplicate key value in a unique or primary key")){
                    notificationLabel.setText("That username already exists. Pick a new one");
                } else{
                    notificationLabel.setText("Unable to create an account at this time. Try again later");
                }
            }finally {
                //safely shutdown database and clear cache
                db.closeDB();
            }
        }
    }

    //function to check user authentication
    public boolean userAuthentication() throws SQLException {
        String username, password;
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";


        username = usernameLogin.getText();
        password = passwordLogin.getText();

        try {
            //set up connection to the StudyEngine database
            conn = db.DBDriver();

            st = conn.prepareStatement(query);
            st.setString(1, username);
            st.setString(2, password);

            rs = st.executeQuery();

            //if rs contains data, user auth confirmed, otherwise invalid credentials
            if(rs.next()){
                //set the global identifier to this users username
                GlobalID.setUserIdentifier(username);
                return true;
            } else{
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            e.getMessage();
            return false;
        }finally {
            db.closeDB();
        }
    }

    //function to send user to main page if credentials match
    public void handleLogin(ActionEvent event) throws IOException, SQLException {
        String username, password;
        username = usernameLogin.getText();
        password = passwordLogin.getText();

        if(username.equals("") || password.equals("")){
            notificationLabel.setText("Make sure all fields are complete");
        }else if(!userAuthentication()){
            notificationLabel.setText("Invalid user credentials");
        }else{
            //retrieve user info
            User user = User.getByUsername(GlobalID.getUserIdentifier());
            User thisUser = new User(user.getID(), user.getFirstName(), user.getLastName(),
            user.getUsername());

            //load main page with valid user credentials
            FXMLLoader loader = new FXMLLoader(App.class.getResource("mainPage.fxml"));
            Stage stage = (Stage)loginButton.getScene().getWindow();
            Parent root = loader.load();

            //set user name to main page when loaded
            MainPageController mainPage = loader.getController();
            mainPage.setName(thisUser.getFirstName());

            //set scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }











    public void connDB(ActionEvent event) throws SQLException {
        DBHandler db = new DBHandler();
        Connection conn = db.DBDriver();
        Statement st = conn.createStatement();
//
//        //tests for user table
//
//        conn.createStatement().execute("INSERT INTO users(firstName, lastName, username, password) VALUES('Matt', 'Prior', 'mprior1', 'password')");
//
//        String query = "SELECT * FROM users";
//        ResultSet rs = st.executeQuery(query);
//        while (rs.next()) {
//            System.out.println(rs.getString("user_id"));
//            System.out.println(rs.getString("firstName"));
//            System.out.println(rs.getString("lastName"));
//            System.out.println(rs.getString("username"));
//            System.out.println(rs.getString("password"));
//        }

//        conn.createStatement().execute("DROP TABLE users");

        //tests for courses table


//        conn.createStatement().execute("INSERT INTO courses(courseName, question, answer, userID) " +
//                "VALUES('Microbiology', 'What is microbiology?', 'I dont know, this is a test', 1)");
//
        String query2 = "SELECT * FROM courses";
        ResultSet rs2 = st.executeQuery(query2);
        while(rs2.next()) {
            System.out.println(rs2.getString("course_id"));
            System.out.println(rs2.getString("courseName"));
            System.out.println(rs2.getString("question"));
            System.out.println(rs2.getString("answer"));
            System.out.println(rs2.getString("userID"));
               }

  //      conn.createStatement().execute("DROP TABLE courses");
//        System.out.println("Success!");
//
//        if(db.tableExists("courses", conn)){
//            String query = "SELECT * FROM courses";
//            ResultSet rs = st.executeQuery(query);
//            while(rs.next()) {
//                System.out.println(rs.getString("course_id"));
//                System.out.println(rs.getString("courseName"));
//                System.out.println(rs.getString("question"));
//                System.out.println(rs.getString("answer"));
//                System.out.println(rs.getString("userID"));
//            }
//        }else{
//            System.out.println("No table exists");
//        }
//    }
    }
}

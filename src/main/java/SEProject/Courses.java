package SEProject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Courses {

    String courseName, question, answer;
    int courseID, userID;

    DBHandler db = new DBHandler();
    Connection conn = null;
    PreparedStatement st = null;
    ResultSet rs = null;

    public Courses(){}

    public Courses(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    //function to return an arraylist of courses created by the user
    public ObservableList<String> getCourseListByUserID(int id){
        String query = "SELECT courseName FROM courses WHERE userID = ?";
        List<String> courses = new ArrayList<>();

        try {
            conn = db.DBDriver();
            st = conn.prepareStatement(query);
            st.setInt(1, id);

            rs = st.executeQuery();
            while(rs.next()){
                courses.add(rs.getString("courseName"));
            }

            courses = removeDuplicates((ArrayList<String>) courses);

        } catch (SQLException e) {
            e.printStackTrace();
            e.getMessage();
        }finally {
            db.closeDB();
        }
        return FXCollections.observableArrayList(courses);
    }

    public ObservableList<Courses> getCourseInfo(String course, int id){
        String query = "SELECT question, answer FROM courses WHERE courseName = ? " +
                "AND userID = ?";

        ObservableList<Courses> list = FXCollections.observableArrayList();

        try {
            conn = db.DBDriver();
            st = conn.prepareStatement(query);
            st.setString(1, course);
            st.setInt(2, id);

            rs = st.executeQuery();
            while(rs.next()){
                list.add(new Courses(rs.getString("question"), rs.getString("answer")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            e.getMessage();
        }finally {
            db.closeDB();
        }

        return list;
    }


    //function to check if a course already exists in the database
    public boolean courseExists(String course, int id){
        String query = "SELECT courseName FROM courses WHERE courseName = ?" +
                " AND userID = ?";

        try {
            conn = db.DBDriver();
            st = conn.prepareStatement(query);
            st.setString(1, course);
            st.setInt(2, id);

            rs = st.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }finally {
            //close db and clear cache
            db.closeDB();
        }

    }


    //function to check if a question already exists in the database
    public boolean questionExists(String question, String course, int id){
        String query = "SELECT question FROM courses WHERE question = ? AND courseName = ?" +
                " AND userID = ?";

        try {
            conn = db.DBDriver();
            st = conn.prepareStatement(query);
            st.setString(1, question);
            st.setString(2, course);
            st.setInt(3, id);

            rs = st.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }finally {
            //close db and clear cache
            db.closeDB();
        }

    }

    //function to remove duplicate strings in course list
    public static ArrayList<String> removeDuplicates(ArrayList<String> list){
        ArrayList<String> newList = new ArrayList<>();

        for(String element : list){

            if(!newList.contains(element)){
                newList.add(element);
            }
        }
        return newList;
    }

    public void deleteFromCourseList(String course, String question, int userID){
        String query = "DELETE FROM courses WHERE courseName = ? " +
                "AND question = ? AND userID = ?";

        try {
            conn = db.DBDriver();
            st = conn.prepareStatement(query);
            st.setString(1, course);
            st.setString(2, question);
            st.setInt(3, userID);

            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            e.getMessage();
        }finally {
            db.closeDB();
        }
    }

    //function to update question and answer for a specific course
    public void updateCourse(String course, String question, String newQuestion, String newAnswer, int userID){
        String query = "UPDATE courses SET question = ?, answer = ? " +
                "WHERE courseName = ? AND question = ? AND userID = ?";

        try {
            conn = db.DBDriver();
            st = conn.prepareStatement(query);
            st.setString(1, newQuestion);
            st.setString(2, newAnswer);
            st.setString(3, course);
            st.setString(4, question);
            st.setInt(5, userID);

            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            e.getMessage();
        }finally {
            db.closeDB();
        }
    }

    //function to retrieve an array of questions from a given course
    public ArrayList<String> getQuestions(String courseName, int userID){
        String query = "SELECT question FROM courses WHERE courseName = ? AND userID = ? " +
                "ORDER BY course_id";

        ArrayList<String> questions = new ArrayList<>();

        try{
            conn = db.DBDriver();
            st = conn.prepareStatement(query);
            st.setString(1, courseName);
            st.setInt(2, userID);

            rs = st.executeQuery();
            while(rs.next()){
                questions.add(rs.getString("question"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            db.closeDB();
        }

        return questions;
    }

    //function to grab the answer associated with a question
    public String grabCorrectAnswer(String courseName, String question, int userID){

        String query = "SELECT answer FROM courses WHERE courseName = ? AND question = ? AND userID = ? ";
        String answer = "";

        try{
            conn = db.DBDriver();
            st = conn.prepareStatement(query);
            st.setString(1, courseName);
            st.setString(2, question);
            st.setInt(3, userID);

            rs = st.executeQuery();
            while(rs.next()){
                answer = rs.getString("answer");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            db.closeDB();
        }

        return answer;
    }

    //function to retrieve list of answers from a given course
    public ArrayList<String> getAnswers(String courseName, int userID){
        String query = "SELECT answer FROM courses WHERE courseName = ? AND userID = ? " +
                "ORDER BY course_id";

        ArrayList<String> answers = new ArrayList<>();

        try{
            conn = db.DBDriver();
            st = conn.prepareStatement(query);
            st.setString(1, courseName);
            st.setInt(2, userID);

            rs = st.executeQuery();
            while(rs.next()){
                answers.add(rs.getString("answer"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            db.closeDB();
        }

        return answers;
    }

    //function to see if an answer is correct
    public boolean isCorrectAnswer(String courseName, String question, String answer, int userID){
        String query = "SELECT * FROM courses WHERE courseName = ? AND question = ? AND answer = ? AND " +
                "userID = ?";

        try {
            conn = db.DBDriver();
            st = conn.prepareStatement(query);
            st.setString(1, courseName);
            st.setString(2, question);
            st.setString(3, answer);
            st.setInt(4, userID);

            rs = st.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            db.closeDB();
        }
        return false;
    }

    //function to check if question exists with the course
//    public boolean questionExistsWithCourse(String courseName, String question, int userID){
//        String query = "SELECT * FROM courses WHERE courseName = ? AND question = ? AND " +
//                "userID = ?";
//
//        try {
//            conn = db.DBDriver();
//            st = conn.prepareStatement(query);
//            st.setString(1, courseName);
//            st.setString(2, question);
//            st.setInt(3, userID);
//
//            rs = st.executeQuery();
//
//            return rs.next();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }finally {
//            db.closeDB();
//        }
//        return false;
//    }

    public String getQuestion(){ return question; }
    public String getAnswer(){ return answer; }

}

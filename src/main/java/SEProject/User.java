package SEProject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;

    //empty constructor
    public User(){};

    //simple User constructor
    public User(int id, String firstName, String lastName, String username){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    public static User getByUsername(String username) throws SQLException {
        User user = null;

        DBHandler db = new DBHandler();
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        String query = "SELECT * FROM users WHERE username = ?";

        try {
            conn = db.DBDriver();
            st = conn.prepareStatement(query);
            st.setString(1, username);
            rs = st.executeQuery();

            while (rs.next()){
                user = new User(
                        rs.getInt("user_id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("username")
                );
            }

            if(user == null){
                System.out.println("that user doesn't exist");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            e.getMessage();
        }finally {
            db.closeDB();
        }

        return user;
    }

    ////////////GETTERS///////////////

    public int getID(){ return id; }
    public String getFirstName(){ return firstName; }
    public String getLastName(){ return lastName; }
    public String getUsername(){ return username; }



}

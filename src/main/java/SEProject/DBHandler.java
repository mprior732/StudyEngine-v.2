package SEProject;

import java.sql.*;

public class DBHandler {

    private Connection conn = null;

    //function to connect to the derby database
    public Connection DBDriver(){
        try{

            String driver = "org.apache.derby.jdbc.EmbeddedDriver";
            Class.forName(driver);

            //set up connection to embedded database
            String dbURL = "jdbc:derby:dbstudyengine;create=true";
            conn = DriverManager.getConnection(dbURL);

            //check if user table exists, otherwise create it
            if(!tableExists("users", conn)) {
                conn.createStatement().execute("CREATE TABLE " +
                        "users(user_id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1)," +
                        "firstName VARCHAR(30), lastName VARCHAR(30), username VARCHAR(30) UNIQUE, password VARCHAR(30))");
            }

            //check if courses table exists, otherwise create it
            if(!tableExists("courses", conn)){
                conn.createStatement().execute("CREATE TABLE " +
                        "courses(course_id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1)," +
                       "courseName VARCHAR(30), question VARCHAR(1000), answer VARCHAR(1000), userID INTEGER)");
            }


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            conn = null;
        } finally {
            if(conn == null){
                System.out.println("Unable to connect to database");
                System.exit(1);
            }

        }
        return conn;
    }

    //function to properly shutdown database and clear cache
    public void closeDB(){
        try {

            String dbURL = "jdbc:derby:dbstudyengine;shutdown=true";
            conn = DriverManager.getConnection(dbURL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Error code: " + e.getErrorCode());
            System.out.println("SQL state: " + e.getSQLState());
        }
    }

    //function to check if a table exists within a given database
    public boolean tableExists(String tableName, Connection conn) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet rs = meta.getTables(null, null, tableName.toUpperCase(), null);

        return rs.next();
    }



}

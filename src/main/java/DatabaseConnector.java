import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

   private static final String URL = "jdbc:mysql://164.92.229.54:3306/retailhub_db"; 
    private static final String USER = "retailhub"; 
    private static final String PASSWORD = "oMada3###ftw"; 

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

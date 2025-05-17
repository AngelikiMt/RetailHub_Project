import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    private static final String URL = "jdbc:mysql://localhost:3306/retailhub_db"; // άλλαξε το αν χρειάζεται
    private static final String USER = "root";         // ή όπως λέγεται ο χρήστης σου
    private static final String PASSWORD = "password"; // βάλε τον δικό σου κωδικό

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

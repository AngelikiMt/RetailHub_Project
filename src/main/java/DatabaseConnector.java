import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnector {

    private static final String PROPERTIES_FILE = "config.properties"; 
    private static String dbUrl;
    private static String dbUser;
    private static String dbPass;

    static {
        try (InputStream input = DatabaseConnector.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            Properties props = new Properties();
            if (input != null) {
                props.load(input);
                dbUrl = props.getProperty("db.url");
                dbUser = props.getProperty("db.username");
                dbPass = props.getProperty("db.password");
            } else {
                throw new RuntimeException("Could not find " + PROPERTIES_FILE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPass);
    }
}

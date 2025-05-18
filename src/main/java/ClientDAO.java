import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class ClientDAO {

    public static void insertClient(Client client) {
        String sql = "INSERT INTO client (first_name, last_name, birth_date, phone_number, email, gender, active_status, date_joined, client_sum_total, last_purchase_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, client.getFirstName());
            stmt.setString(2, client.getLastName());
            stmt.setDate(3, java.sql.Date.valueOf(client.getBirthDate()));
            stmt.setString(4, client.getPhoneNumber());
            stmt.setString(5, client.getEmail());
            stmt.setString(6, client.getGender());
            stmt.setBoolean(7, client.isActiveStatus());
            stmt.setDate(8, java.sql.Date.valueOf(client.getDateJoined()));
            stmt.setDouble(9, client.getClientSumTotal());

            if (client.getLastPurchaseDate() != null) {
                stmt.setDate(10, java.sql.Date.valueOf(client.getLastPurchaseDate()));
            } else {
                stmt.setNull(10, java.sql.Types.DATE);
            }

            stmt.executeUpdate();
            System.out.println("✅ Client inserted into database.");

        } catch (SQLException e) {
            System.err.println("❌ Error inserting client: " + e.getMessage());
        }
    }
}

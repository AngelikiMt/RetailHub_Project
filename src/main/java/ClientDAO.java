import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    public boolean insertClient(Client client) {
        String sql = "INSERT INTO client (firstName, lastName, birthDate, phoneNumber, email, gender, activeStatus, dateJoined, clientSumTotal, lastPurchaseDate) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, client.getFirstName());
            stmt.setString(2, client.getLastName());
            stmt.setDate(3, Date.valueOf(client.getBirthDate()));
            stmt.setString(4, client.getPhoneNumber());
            stmt.setString(5, client.getEmail());
            stmt.setString(6, client.getGender());
            stmt.setBoolean(7, client.isActiveStatus());
            stmt.setDate(8, Date.valueOf(client.getDateJoined()));
            stmt.setDouble(9, client.getClientSumTotal());

            if (client.getLastPurchaseDate() != null) {
                stmt.setDate(10, Date.valueOf(client.getLastPurchaseDate()));
            } else {
                stmt.setNull(10, Types.DATE);
            }

            int rows = stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                long id = rs.getLong(1);
                java.lang.reflect.Field field = Client.class.getDeclaredField("clientId");
                field.setAccessible(true);
                field.set(client, id);
            }

            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM client";

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                clients.add(map(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clients;
    }

    public Client getClientById(long clientId) {
        String sql = "SELECT * FROM client WHERE clientId = ?";

        try (Connection conn = DatabaseConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, clientId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return map(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Client getClientByEmailOrPhone(String input) {
        String sql = "SELECT * FROM client WHERE email = ? OR phoneNumber = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, input);
            stmt.setString(2, input);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return map(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateClient(Client client) {
        String sql = "UPDATE client SET firstName = ?, lastName = ?, phoneNumber = ?, email = ?, birthDate = ? WHERE clientId = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, client.getFirstName());
            stmt.setString(2, client.getLastName());
            stmt.setString(3, client.getPhoneNumber());
            stmt.setString(4, client.getEmail());
            stmt.setDate(5, Date.valueOf(client.getBirthDate()));
            stmt.setLong(6, client.getClientId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean anonymizeClient(long clientId) {
        String sql = "UPDATE client SET activeStatus = false, email = NULL, phoneNumber = NULL, birthDate = NULL, firstName = NULL, lastName = NULL WHERE clientId = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, clientId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public int deleteClientsInactiveMoreThan5Years() {
        String sql = "DELETE FROM client WHERE lastPurchaseDate IS NOT NULL AND lastPurchaseDate < ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            LocalDate fiveYearsAgo = LocalDate.now().minusYears(5);
            stmt.setDate(1, Date.valueOf(fiveYearsAgo));

            return stmt.executeUpdate(); // returns number of rows deleted

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private Client map(ResultSet rs) throws SQLException {
        Client client = new Client(
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getDate("birthDate") != null ? rs.getDate("birthDate").toLocalDate() : null,
                rs.getString("phoneNumber"),
                rs.getString("email"),
                rs.getString("gender"),
                rs.getBoolean("activeStatus")
        );

        try {
            java.lang.reflect.Field field = Client.class.getDeclaredField("clientId");
            field.setAccessible(true);
            field.set(client, rs.getLong("clientId"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        client.setClientSumTotal(rs.getDouble("clientSumTotal"));
        Date lastPurchase = rs.getDate("lastPurchaseDate");
        if (lastPurchase != null) {
            client.setLastPurchaseDate(lastPurchase.toLocalDate());
        }

        return client;
    }
}

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
        String sql = "INSERT INTO client (firstName, lastName, birthDate, phoneNumber, email, gender, activeStatus, dateJoined, clientSumTotal, clientCurrentTotal, lastPurchaseDate) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
            stmt.setDouble(10, client.getClientCurrentTotal());

            if (client.getLastPurchaseDate() != null) {
                stmt.setDate(11, Date.valueOf(client.getLastPurchaseDate()));
            } else {
                stmt.setNull(11, Types.DATE);
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
        String sql = "UPDATE client SET firstName = ?, lastName = ?, phoneNumber = ?, email = ?, birthDate = ?, clientSumTotal = ?, clientCurrentTotal = ?, lastPurchaseDate = ? WHERE clientId = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, client.getFirstName());
            stmt.setString(2, client.getLastName());
            stmt.setString(3, client.getPhoneNumber());
            stmt.setString(4, client.getEmail());
            stmt.setDate(5, Date.valueOf(client.getBirthDate()));
            stmt.setDouble(6, client.getClientSumTotal());
            stmt.setDouble(7, client.getClientCurrentTotal());
            stmt.setDate(8, Date.valueOf(client.getLastPurchaseDate()));
            stmt.setLong(9, client.getClientId());

            LocalDate birthDate = client.getBirthDate();
            if (birthDate != null) {
                stmt.setDate(5, Date.valueOf(birthDate));
            } else {
                stmt.setNull(5, Types.DATE); // Set SQL NULL if birthDate is null
            }
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean anonymizeClient(long clientId) {
    
        String sql = "UPDATE client SET activeStatus = false, email = ?, phoneNumber = ?, birthDate = NULL, firstName = 'deleted', lastName = 'deleted' WHERE clientId = ?";

        String anonymizedEmail = "deleted_" + clientId + "@example.com";
        String anonymizedPhone = "000000" + clientId;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, anonymizedEmail);
            stmt.setString(2, anonymizedPhone);    
            stmt.setLong(3, clientId);
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

       // Retrieves store data and formats it into a simple JSON string
    public String getClientAsJson(long clientId) {
        StringBuilder json = new StringBuilder();
        
        String sql = "SELECT * FROM client WHERE clientId = ?";
        try (Connection conn = DatabaseConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, clientId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                json.append("{\n  \"clientId\": ").append(rs.getLong("clientId")).append(",\n");
                json.append("  \"firstName\": ").append(rs.getString("firstName")).append(",\n");
                json.append("  \"lastName\": ").append(rs.getString("lastName")).append(",\n");
                json.append("  \"birthDate\": ").append(rs.getDate("birthDate")).append(",\n");
                json.append("  \"phoneNumber\": ").append(rs.getString("phoneNumber")).append(",\n");
                json.append("  \"email\": ").append(rs.getString("email")).append(",\n");
                json.append("  \"gender\": ").append(rs.getString("gender")).append(",\n");
                json.append("  \"activeStatus\": ").append(rs.getBoolean("activeStatus")).append(",\n");
                json.append("  \"dateJoined\": ").append(rs.getDate("dateJoined")).append(",\n");
                json.append("  \"clientSumTotal\": ").append(rs.getDouble("clientSumTotal")).append(",\n");
                json.append("  \"clientCurrentTotal\": ").append(rs.getDouble("clientCurrentTotal")).append(",\n");
                json.append("  \"lastPurchaseDate\": ").append(rs.getDate("lastPurchaseDate")).append("\n}");
            } else {
                return "{}"; // No store found
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return json.toString();
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
        client.setClientCurrentTotal(rs.getDouble("clientCurrentTotal"));
        Date lastPurchase = rs.getDate("lastPurchaseDate");
        if (lastPurchase != null) {
            client.setLastPurchaseDate(lastPurchase.toLocalDate());
        }
        return client;
    }
}

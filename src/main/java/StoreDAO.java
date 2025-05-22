import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StoreDAO {

    // Insert a new store
    public boolean createStore(Store store) {
        String sql = "INSERT INTO store (storeName, address, country, phone, active) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, store.getStoreName());
            stmt.setString(2, store.getAddress());
            stmt.setString(3, store.getCountry());
            stmt.setString(4, store.getPhone());
            stmt.setBoolean(5, store.isActive());

            int affectedRows = stmt.executeUpdate();

           // Retrieve the auto-generated storeId from the database
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1); // Get as int if storeId is int
                    store.setStoreId(id); // Set the auto-generated ID back to the Store object using the public setter
                }
            }

            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Print full stack trace for debugging
            throw new RuntimeException("Failed to create store in DAO: " + e.getMessage(), e);
        }
    }
    // Retrieve all stores
    public List<Store> getAllStores() {
        List<Store> stores = new ArrayList<>();
        String sql = "SELECT * FROM store";

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement()) {
            
            ResultSet rs = stmt.executeQuery(sql); 

            while (rs.next()) {
                stores.add(mapResultSetToStore(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve all stores: " + e.getMessage(), e);
        }
        return stores;
    }

    // Find store by ID
    public Store getStoreById(int id) {
        String sql = "SELECT * FROM store WHERE storeId = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToStore(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve store by ID: " + e.getMessage(), e);
        }
        return null;
    }

    // Update store
    public boolean updateStore(Store store) {
        String sql = "UPDATE store SET storeName = ?, address = ?, country = ?, phone = ?, active = ? WHERE storeId = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, store.getStoreName());
            stmt.setString(2, store.getAddress());
            stmt.setString(3, store.getCountry());
            stmt.setString(4, store.getPhone());
            stmt.setBoolean(5, store.isActive());
            stmt.setInt(6, store.getStoreId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update store in DAO: " + e.getMessage(), e);
        }
    }

    public boolean setStoreActive(int storeId, boolean active) {
        String sql = "UPDATE store SET active = ? WHERE productId = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, active);
            stmt.setLong(2, storeId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set store active status in DAO: " + e.getMessage(), e);
        }
    }

    public String getStoreAsJson(int storeId) {
        StringBuilder json = new StringBuilder();
        
        String sql = "SELECT * FROM store WHERE storeId = ?";
        try (Connection conn = DatabaseConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, storeId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                json.append("{\n  \"storeId\": ").append(storeId).append(",\n");
                json.append("  \"phone\": ").append(rs.getString("phone")).append(",\n");
                json.append("  \"address\": ").append(rs.getString("address")).append(",\n");
                json.append("  \"country\": ").append(rs.getString("country")).append(",\n");
                json.append("  \"storeName\": ").append(rs.getString("storeName")).append(",\n");
                json.append("  \"active\": ").append(rs.getBoolean("active")).append("\n}");

            } else {
                return "{}"; // No store found
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return json.toString();
    }

    private Store mapResultSetToStore(ResultSet rs) throws SQLException {
        int storeId = rs.getInt("storeId");
        String storeName = rs.getString("storeName");
        String address = rs.getString("address");
        String country = rs.getString("country");
        String phone = rs.getString("phone");
        boolean active = rs.getBoolean("active");

        // Use the constructor that accepts storeId and all other fields
        return new Store(storeId, storeName, address, country, phone, active);
    }
}

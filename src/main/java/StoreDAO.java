/* Handles the direct interaction with the database for the Store objects. It abstracts away the JDBC (Java Database Connectivity) details from the rest of the application. It is responsible for performing CRUD operations on the Store table in the database. */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StoreDAO {

    // Create a new store
    public boolean createStore(Store store) {
        String sql = "INSERT INTO store (storeName, address, city, country, phone, active) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, store.getStoreName());
            stmt.setString(2, store.getAddress());
            stmt.setString(3, store.getCity());
            stmt.setString(4, store.getCountry());
            stmt.setString(5, store.getPhone());
            stmt.setBoolean(6, store.isActive());

            int affectedRows = stmt.executeUpdate();

           // Retrieve the auto-generated storeId from the database
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1); // Retrieves the auto-generated primary key (storeId) from the DB after insertion.
                    store.setStoreId(id); // Set the auto-generated ID back to the Store object using the public setter
                }
            }

            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Print full stack trace for debugging
            throw new RuntimeException("Failed to create store in DAO: " + e.getMessage(), e);
        }
    }
    // Retrieve all stores and returns a list List<Store>
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

    //  Retrieves a single store by ID and returns a list List<Store>
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

    // Update an existing store using the provided Store object's data
    public boolean updateStore(Store store) {
        String sql = "UPDATE store SET storeName = ?, address = ?, city =?, country = ?, phone = ?, active = ? WHERE storeId = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, store.getStoreName());
            stmt.setString(2, store.getAddress());
            stmt.setString(3, store.getCity());
            stmt.setString(4, store.getCountry());
            stmt.setString(5, store.getPhone());
            stmt.setBoolean(6, store.isActive());
            stmt.setInt(7, store.getStoreId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update store in DAO: " + e.getMessage(), e);
        }
    }

    // Updates the active status of a store usign storeId
    public boolean setStoreActive(int storeId, boolean active) {
        String sql = "UPDATE store SET active = ? WHERE storeId = ?";
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

    // Retrieves store data and formats it into a simple JSON string
    public String getStoreAsJson(int storeId) {
        StringBuilder json = new StringBuilder();
        
        String sql = "SELECT * FROM store WHERE storeId = ?";
        try (Connection conn = DatabaseConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, storeId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                json.append("{\n  \"storeId\": ").append(rs.getInt("storeId")).append(",\n");
                json.append("  \"phone\": ").append(rs.getString("phone")).append(",\n");
                json.append("  \"address\": ").append(rs.getString("address")).append(",\n");
                json.append("  \"city\": ").append(rs.getString("city")).append(",\n");
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

    // A helper method. It takes a ResultSet from the DB query and maps its current row's data to a new Store object.
    private Store mapResultSetToStore(ResultSet rs) throws SQLException {
        int storeId = rs.getInt("storeId");
        String storeName = rs.getString("storeName");
        String address = rs.getString("address");
        String city = rs.getString("city");
        String country = rs.getString("country");
        String phone = rs.getString("phone");
        boolean active = rs.getBoolean("active");

        // The constructor that accepts storeId and all other fields
        return new Store(storeId, storeName, address, city, country, phone, active);
    }
}

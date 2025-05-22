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
        String sql = "INSERT INTO stores (storeName, address, country, phone, active) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, store.getStoreName());
            stmt.setString(2, store.getAddress());
            stmt.setString(3, store.getCountry());
            stmt.setString(4, store.getPhone());
            stmt.setBoolean(5, store.isActive());

            int affectedRows = stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                long id = generatedKeys.getLong(1);
                try {
                    java.lang.reflect.Field field = Store.class.getDeclaredField("storeId");
                    field.setAccessible(true);
                    field.set(store, id);
                } catch (NoSuchFieldException | IllegalAccessException e){
                    e.printStackTrace();
                }
            }

            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Retrieve all stores
    public List<Store> getAllStores() {
        List<Store> stores = new ArrayList<>();
        String sql = "SELECT * FROM stores";

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement()) {
            
            ResultSet rs = stmt.executeQuery(sql); 

            while (rs.next()) {
                stores.add(mapResultSetToStore(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stores;
    }

    // Find store by ID
    public Store getStoreById(int id) {
        String sql = "SELECT * FROM stores WHERE storeId = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToStore(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Update store
    public boolean updateStore(Store store) {
        String sql = "UPDATE stores SET storeName = ?, address = ?, country = ?, phone = ?, active = ? WHERE storeId = ?";

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
        }
        return false;
    }

    public boolean seStoreActive(long storeId, boolean active) {
        String sql = "UPDATE store SET active = ? WHERE productId = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, active);
            stmt.setLong(2, storeId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getStoreAsJson(long storeId) {
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
        long storeId = rs.getInt("storeId");
        String storeName = rs.getString("storeName");
        String address = rs.getString("address");
        String country = rs.getString("country");
        String phone = rs.getString("phone");
        boolean active = rs.getBoolean("active");

        Store store = new Store(storeName, address, country, phone);
        store.setActive(active);

        try {
            java.lang.reflect.Field field = Store.class.getDeclaredField("storeId");
            field.setAccessible(true);
            field.set(store, storeId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return store;
    }
}

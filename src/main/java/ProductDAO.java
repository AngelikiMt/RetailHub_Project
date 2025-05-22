import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public boolean createProduct(Product product) {
        String sql = "INSERT INTO products (description, category, price, cost, active) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                
            stmt.setString(1, product.getDescription());
            stmt.setString(2, product.getCategory());
            stmt.setDouble(3, product.getPrice());
            stmt.setDouble(4, product.getCost());
            stmt.setBoolean(5, product.getActive());
            
            int rows = stmt.executeUpdate();
            // Retrieve the auto-generated productId from the database
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    product.setProductId(id);
                }
            }
            return rows > 0;
        } catch (SQLException  e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create product in DAO: " + e.getMessage(), e);
        }
    }

    public Product getProductById(long productId) {
        String sql = "SELECT * FROM products WHERE productId = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToProduct(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve product by ID: " + e.getMessage(), e);
        }
        return null;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve all products: " + e.getMessage(), e);
        }
        return products;
    }

    public boolean updateProduct(Product product) {
        String sql = "UPDATE products SET description = ?, category = ?, price = ?, cost = ?, active = ? WHERE productId = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getDescription());
            stmt.setString(2, product.getCategory());
            stmt.setDouble(3, product.getPrice());
            stmt.setDouble(4, product.getCost());
            stmt.setBoolean(5, product.getActive());
            stmt.setLong(6, product.getProductId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update product in DAO: " + e.getMessage(), e);
        }
    }

    public boolean deleteProduct(long productId) {
        String sql = "DELETE FROM products WHERE productId = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, productId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete product in DAO: " + e.getMessage(), e);
        }
    }

    public boolean setProductActive(long productId, boolean active) {
        String sql = "UPDATE products SET active = ? WHERE productId = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, active);
            stmt.setLong(2, productId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set product active status in DAO: " + e.getMessage(), e);
        }
    }

    public String getProductAsJson(long productId) {
        StringBuilder json = new StringBuilder();
        
        String sql = "SELECT * FROM products WHERE productId = ?";
        try (Connection conn = DatabaseConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, productId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                json.append("{\n  \"productId\": ").append(productId).append(",\n");
                json.append("  \"description\": ").append(rs.getString("description")).append(",\n");
                json.append("  \"category\": ").append(rs.getString("category")).append(",\n");
                json.append("  \"price\": ").append(rs.getDouble("price")).append(",\n");
                json.append("  \"cost\": ").append(rs.getDouble("cost")).append(",\n");
                json.append("  \"active\": ").append(rs.getBoolean("active")).append("\n}");

            } else {
                return "{}"; // No product found
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return json.toString();
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        long id = rs.getLong("productId");
        String description = rs.getString("description");
        String category = rs.getString("category");
        double price = rs.getDouble("price");
        double cost = rs.getDouble("cost");
        boolean active = rs.getBoolean("active");

        return new Product(id, description, category, price, cost, active);
    }
}

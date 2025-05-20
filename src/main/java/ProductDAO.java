/*  Connects Product.Java and ProductService.java with the MySQL database.
 *  Handles: Inserting, Retrieving, Updating, Deleting a product and Retrieving all products (CRUD).
*/

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    /*  Retrieves and prints the productId */
    public void insertProduct(Product product) {
        String sql = "INSERT INTO products (description, category, price, cost, active) VALUES (?, ?, ?, ?, ?)";
        try(Connection conn = DatabaseConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getDescription());
            stmt.setString(2, product.getCategory());
            stmt.setDouble(3, product.getPrice());
            stmt.setDouble(4, product.getCost());
            stmt.setBoolean(5, product.getActive());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                long id = rs.getLong(1);
                System.out.println("Inserted product with ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    /* Fetches a single product by it's ID.
        Converts the result set into a Product object using mapResultSetToProduct */    }

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
        }
        return null;
    }

    /* Returns a list of all products. Fills an ArrayList<Product> with results. */
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
        }
        return products;
    }

    /* Updates product details using the product's ID. */
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
        }
        return false;
    }

    /* Deletes ther product from the database by ID. */
    public boolean deleteProduct(long productId) {
        String sql = "DELETE FROM products WHERE productId = ?";
        try (Connection conn = DatabaseConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, productId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /* A helper method. Converts a database row(ResultSet) to a Product object.
     * It uses reflection to override the private field productId.
     */
    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        long id = rs.getLong("productId");
        String description = rs.getString("description");
        String category = rs.getString("category");
        double price = rs.getDouble("price");
        double cost = rs.getDouble("cost");
        boolean active = rs.getBoolean("active");

        Product product = new Product(description, category, price, cost);

        try {
            java.lang.reflect.Field field = Product.class.getDeclaredField("productId");
            field.setAccessible(true);
            field.set(product, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        product.setActive(active);
        return product;
    }
}

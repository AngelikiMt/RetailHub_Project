
/*  Connects Stock.Java and StockService.java with the MySQL database.
 *  Handles: Inserting, Retrieving, Updating, Reducing stocks (CRUD), Searches for a produc
 *  with low stock, for a doduct in other stores.
*/

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StockDAO {

    /* Adds a stock into the database */
    public void insertStock(Stock stock) {
        String sql = "INSERT INTO stock (storeId, productId, stockQuantity, activeFlag) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, stock.getStoreId());
            stmt.setLong(2, stock.getProductId());
            stmt.setInt(3, stock.getStockQuantity());
            stmt.setBoolean(4, stock.isActiveFlag());

            stmt.executeUpdate();
            System.out.println("Stock added successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* Fetches a stock by it's store ID and product ID.
     * Converts the result set into a Stock object using new Stock() */    
    public Stock getStock(int storeId, long productId) {
        String sql = "SELECT * FROM stock WHERE storeId = ? and productId = ?";

        try (Connection conn = DatabaseConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, storeId);
            stmt.setLong(2, productId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Stock(
                    rs.getInt("storeId"),
                    rs.getLong("productId"),
                    rs.getInt("stockQuantity"),
                    rs.getBoolean("activeFlag"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* Updates stock details using store's ID, product's ID and the quantity to be added. */
    public void updateStock(int storeId, long productId, int quantity) {
        String sql = "UPDATE stock SET stockQuantity = stockQuantity + ? WHERE storeId = ? AND productId = ?";

        try (Connection conn = DatabaseConnector.getConnection(); 
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, storeId);
            stmt.setLong(3, productId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* Reduces the product's stock in the database. */
    public void reduceStock(int storeId, long productId, int quantity) {
        String sql = "UPDATE stock SET stockQuantity = stockQuantity - ? WHERE storeId = ? AND productId = ? AND stockQuantity >= ?";

        try (Connection conn = DatabaseConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, storeId);
            stmt.setLong(3, productId);
            stmt.setInt(4, quantity);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* Returns a list with all products with stock lower than threshold. */
    public List<Stock> getLowStockProducts(int threshold) {
        List<Stock> lowStockList = new ArrayList<>();
        String sql = "SELECT * FROM stock WHERE stockQuantity <= ?";
        try (Connection conn = DatabaseConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, threshold);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lowStockList.add(new Stock(
                    rs.getInt("storeId"),
                    rs.getLong("productId"),
                    rs.getInt("stockQuantity"),
                    rs.getBoolean("activeFlag")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lowStockList;
    }

    /* Searches for a product in other stores except of the excluded one. Returns a list with stocks of the specific product. */
    public List<Stock> searchProductInOtherStores(long productId, int excludedStoreId) {
        List<Stock> result = new ArrayList<>();
        String sql = "SELECT * FROM stock WHERE productId = ? AND storeId !=? AND activeFlag = TRUE AND stockQuantity > 0";

        try (Connection conn = DatabaseConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, productId);
            stmt.setInt(2, excludedStoreId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(new Stock(
                    rs.getInt("storeId"),
                    rs.getLong("productId"),
                    rs.getInt("stockQuantity"),
                    rs.getBoolean("activeFlag")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    /* Retrieves stock info from the database for the product ID given. Builds and returns info in JSON format. */
    public String getStockAsJson(long productId) {
        StringBuilder json = new StringBuilder();

        String sql = "SELECT storeId, stockQuantity FROM stock WHERE productId = ?";
        try (Connection conn = DatabaseConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, productId);

            ResultSet rs = stmt.executeQuery();

            boolean firstEntryForProduct = true; // Flag to handle the first entry of the product
            boolean productHasStock = false; // Flag to check if any stock was found for the product

            if (rs.isBeforeFirst()) { // Checks if there are any rows to process
                json.append("{\n  \"productId\": ").append(productId).append(",\n  \"stockPerStore\": [\n");
            }

            while (rs.next()) { // Iterates through all rows
                productHasStock = true; // Mark that stock was found for this product
                if (!firstEntryForProduct) {
                    json.append(",\n"); // Add comma for subsequent entries
                }
                json.append("    { \"storeId\": ").append(rs.getInt("storeId")).append(", \"quantity\": ").append(rs.getInt("stockQuantity")).append(" }");
                firstEntryForProduct = false; // After the first entry, set this to false
                }

                if (productHasStock) {
                    json.append("\n  ]\n}");
                } else {
                    return "{}"; // No stock found for the given productId
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "{ \"error\": \"Database error: " + e.getMessage() + "\" }"; // Returns an error JSON
        }
        return json.toString();
    }
}

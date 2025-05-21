import java.util.*;

public class StockService {
    private static final int THRESHOLD = 3;

    private static final ProductDAO productDAO = new ProductDAO();
    private static final StoreDAO storeDAO = new StoreDAO();
    private static final StockDAO stockDAO = new StockDAO();

    // Add a new stock to the database
    public static void addStock(int storeId, long productId, int stockQuantity) {
        if (storeId <= 0 || productId <= 0 || stockQuantity < 0) {
            System.out.println("Invalid input. Store ID, Product ID must be positive and quantity non-negative.");
            return;
        }

        // Validate store
        Store store = storeDAO.getStoreById(storeId);
        if (store == null || !store.isActive()) {
            System.out.println("Store ID " + storeId + " is invalid or inactive.");
            return;
        }

        // Validate product
        Product product = productDAO.getProductById(productId);
        if (product == null || !product.getActive()) {
            System.out.println("Product ID " + productId + " is invalid or inactive.");
            return;
        }

        // Check if stock already exists
        Stock existingStock = stockDAO.getStock(storeId, productId);
        if (existingStock != null) {
            System.out.println("Stock already exists for this product in the given store.");
            return;
        }

        // Insert new stock
        Stock stock = new Stock(storeId, productId, stockQuantity, true);
        stockDAO.insertStock(stock);
    }

    // Get stock by product and store
    public static Stock getStock(long productId, int storeId) {
        return stockDAO.getStock(storeId, productId);
    }

    // Update stock quantity
    public static void updateStock(long productId, int storeId, int newQuantity) {
        if (newQuantity < 0) {
            System.out.println("Quantity cannot be negative.");
            return;
        }

        Stock existingStock = stockDAO.getStock(storeId, productId);
        if (existingStock == null) {
            System.out.println("Stock does not exist for this product in the given store.");
            return;
        }

        stockDAO.updateStock(storeId, productId, newQuantity);
        System.out.println("Stock updated successfully.");
    }

    // Reduce stock on purchase
    public static void reduceStockOnPurchase(long productId, int storeId, int quantity) {
        if (quantity <= 0) {
            System.out.println("Reduction quantity must be greater than zero.");
            return;
        }

        Stock stock = stockDAO.getStock(storeId, productId);
        if (stock == null) {
            System.out.println("No inventory found for product ID: " + productId + " in store ID: " + storeId);
            return;
        }

        if (stock.getStockQuantity() < quantity) {
            System.out.println("Insufficient inventory for product ID: " + productId);
            return;
        }

        stockDAO.reduceStock(storeId, productId, quantity);
        System.out.println("Stock reduced successfully.");
    }

    // Display low stock products (below threshold)
    public static void getLowStockProducts() {
        List<Stock> lowStockList = stockDAO.getLowStockProducts(THRESHOLD);
        if (lowStockList.isEmpty()) {
            System.out.println("No product with low quantity.");
            return;
        }

        for (Stock s : lowStockList) {
            System.out.println("Low stock: Product ID " + s.getProductId() +
                " in Store ID " + s.getStoreId() + " with Quantity " + s.getStockQuantity());
        }
    }

    // Search for a product in other stores (except one)
    public static List<Stock> searchProductInOtherStores(long productId, int excludedStoreId) {
        List<Stock> result = stockDAO.searchProductInOtherStores(productId, excludedStoreId);
        if (result.isEmpty()) {
            System.out.println("Product not available in other stores.");
        } else {
            for (Stock s : result) {
                System.out.println("Product available in Store ID: " + s.getStoreId() +
                    " | Quantity: " + s.getStockQuantity());
            }
        }
        return result;
    }

    // Get stock as JSON string
    public static String getStockAsJson(long productId) {
        return stockDAO.getStockAsJson(productId);
    }
}
import java.util.*;

public class StockService {
    private static List<Stock> stocks = new ArrayList<Stock>();
    private static ProductService productService=new ProductService();

    private static final int threshold=3;
    

    // Add a stock type object to the list of all stocks
    public static void addStock(int storeId, long productId, int stockQuantity) { 
    	if (storeId <= 0 || productId <= 0) {
            System.out.println("Invalid store or product.");
            return;
        }
        //Check if the store exist
        if (!StoreService.validateId(storeId)) {
         System.out.println("Cannot add stock. Store ID " + storeId + " does not exist.");
         return;
        }

        // Check if the product exists
        Product product = productService.findProductById(productId);
        if (product == null) {
            System.out.println("Cannot add stock. Product ID " + productId + " does not exist.");
            return;
        }

        if (stockQuantity < 0) {
            System.out.println("The inventory quantity cannot be negative.");
            return;
        }

        for (Stock s : stocks) {
            if (productId == s.getProductId() && storeId == s.getStoreId()) {
                System.out.println("This product is already added in this store.");
                return;
            } 
        }
        
    	stocks.add(new Stock(storeId, productId, stockQuantity, true));
        System.out.println("Stock added successfully for Product(ID): " + productId + " in Store(ID): " + storeId + " with Quantity: " + stockQuantity);
    }
 

    // Inventory control for a specific product and store 
    public static List<Stock> getStock(long productId, int storeId) {   
        List<Stock> result = new ArrayList<>();

        if (stocks != null && !stocks.isEmpty()) { 
            for (Stock s : stocks) {
                if (s.getProductId() == productId && s.getStoreId() == storeId) {
                    result.add(s);
                }
            }
        }
        return result;  
    } 

    
    // Update stock
    public static void updateStock(long productId, int storeId, int newQuantity) {  
        List<Stock>currentStocks=getStock(productId, storeId);
        if (currentStocks!=null){
            for(Stock s : currentStocks){
                s.setStockQuantity(newQuantity);
            }
        }
    }

    
    // Reduce stock
    public static void reduceStockOnPurchase(long productId, int storeId, int quantity) { 
        List<Stock> stockList = getStock(productId, storeId);
        if (!stockList.isEmpty()) {
            // Take the first available stock for the product and store
            Stock stock = stockList.get(0);
            if (stock.getStockQuantity() >= quantity) {
                stock.reduceStock(quantity);
            } 
            else {
                System.out.println("Insufficient inventory for product ID: " + productId);
            }
        } 
        else {
            System.out.println("No inventory found for product ID: " + productId + " στο store ID: " + storeId);
        }
    }
    

    // GET stocks with stock below 3
    public static void getLowStockProducts() {      
       
        for (Stock s : stocks) {
            if (s.getStockQuantity() <= threshold) {
                System.out.println("The product with code: " + s.getProductId() + " in store: " + s.getStoreId() + " has low inventory: " + s.getStockQuantity());
                return;
            }
        }
        System.out.println("No product with low quantity.");
    }

    // Searches if the product is available in another store
    public static List<Stock> searchProductInOtherStores(long productId, int excludedStoreId) {
        ArrayList<Stock> result = new ArrayList<>();  // New list for results
    
        for (Stock s : stocks) {  
            if (s.getProductId() == productId &&  // If the product id matches
                s.getStoreId() != excludedStoreId &&  // If it is not the exempt store
                s.isActiveFlag() &&  // If the product is active
                s.getStockQuantity() > 0) {  // If there is stock
    
                result.add(s);  // Adds the stock to the results list
    
                // Print inventory information
                System.out.println("Product ID: " + s.getProductId() +
                                   ", Store ID: " + s.getStoreId() +
                                   ", Quantity: " + s.getStockQuantity());
            }
        }
    
        return result;  // Returns the list of results
    }
    
    // Returns the inventory of a product in JSON format, for all stores that have it available
    public static String getStockAsJson(long productId) {  
        StringBuilder json = new StringBuilder();
        json.append("{\n  \"productId\": ").append(productId).append(",\n  \"stockPerStore\": [\n");
    
        // Creates the inventory list for the specific product
        ArrayList<Stock> productStocks = new ArrayList<>();
        for (Stock s : stocks) {
            if (s.getProductId() == productId) {
                productStocks.add(s);
            }
        }
    
        // Generates the JSON string based on the stocks
        for (int i = 0; i < productStocks.size(); i++) {
            Stock s = productStocks.get(i);
            json.append("    { \"storeId\": ").append(s.getStoreId())
                .append(", \"quantity\": ").append(s.getStockQuantity()).append(" }");
            if (i < productStocks.size() - 1) json.append(",");
            json.append("\n");
        }
    
        json.append("  ]\n}");
        return json.toString();
    }
    
}

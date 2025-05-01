import java.util.*;
import java.util.stream.Collectors;

public class StockService {
    private List<Stock> stocks = new ArrayList<Stock>();

    private static final int threshold=3;

    // Προσθήκη αντικειμένου τύπου stock στην λίστα με όλα τα αποθέματα
    public void addStock(long storeId, long productId, int stockQuantity) { 
    	if (storeId <= 0 || productId <= 0) {
            System.out.println("Invalid store or product.");
            return;
        }

        if (stockQuantity < 0) {
            System.out.println("The inventory quantity cannot be negative.");
            return;
        }
    	
    	stocks.add(new Stock(productId, storeId, stockQuantity, true));
    }
    

    //Έλεγχος αποθέματος για συγκεκριμένο προιόν και κατάστημα 
    public List<Stock> getStock(long productId, long storeId) {   
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

    
    // Ενημέρωση αποθέματος 
    public void updateStock(long productId, long storeId, int newQuantity) {  
        List<Stock>currentStocks=getStock(productId, storeId);
        if (currentStocks!=null){
            for(Stock s : currentStocks){
                s.setStockQuantity(newQuantity);
            }
        }
    }

    
    //Μειώνει το απόθεμα
    public void reduceStockOnPurchase(long productId, long storeId, int quantity) { 
        List<Stock> stockList = getStock(productId, storeId);
        if (!stockList.isEmpty()) {
            // Παίρνουμε το πρώτο διαθέσιμο απόθεμα για το προϊόν και το κατάστημα
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
    

    //GET αποθέματα με stock κάτω από 3
    public void getLowStockProducts() {      
       
        for (Stock s : stocks) {
            if (s.getStockQuantity() <= threshold) {
                System.out.println("The product with code: " + s.getProductId() + " in store: " + s.getStoreId() + " has low inventory: " + s.getStockQuantity());
            }
        }
    }

    // Αναζητά εάν υπάρχει το προιόν σε άλλο κατάστημα διαθέσιμο
    public List<Stock> searchProductInOtherStores(long productId, long excludedStoreId) {
        ArrayList<Stock> result = new ArrayList<>();  // Νέα λίστα για τα αποτελέσματα
    
        for (Stock s : stocks) {  
            if (s.getProductId() == productId &&  // Αν το προϊόν ταιριάζει
                s.getStoreId() != excludedStoreId &&  // Αν δεν είναι το εξαιρούμενο κατάστημα
                s.isActiveFlag() &&  // Αν είναι ενεργό το προϊόν
                s.getStockQuantity() > 0) {  // Αν υπάρχει απόθεμα
    
                result.add(s);  // Προσθέτει το απόθεμα στην λίστα αποτελεσμάτων
    
                // Εκτύπωση πληροφοριών του αποθέματος
                System.out.println("Product ID: " + s.getProductId() +
                                   ", Store ID: " + s.getStoreId() +
                                   ", Quantity: " + s.getStockQuantity());
            }
        }
    
        return result;  // Επιστρέφει τη λίστα με τα αποτελέσματα
    }
    
    //Θα επιστρέψει το απόθεμα ενός προϊόντος σε JSON μορφή, για όλα τα καταστήματα που το έχουν διαθέσιμο
    public String getStockAsJson(long productId) {  
        StringBuilder json = new StringBuilder();
        json.append("{\n  \"productId\": ").append(productId).append(",\n  \"stockPerStore\": [\n");
    
        // Δημιουργία της λίστας των αποθεμάτων για το συγκεκριμένο προϊόν
        ArrayList<Stock> productStocks = new ArrayList<>();
        for (Stock s : stocks) {
            if (s.getProductId() == productId) {
                productStocks.add(s);
            }
        }
    
        // Δημιουργία του JSON string με βάση τα αποθέματα
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
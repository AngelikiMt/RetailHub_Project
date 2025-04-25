import java.util.*;
import java.util.stream.Collectors;

public class StockService {
    private List<Stock> stocks = new ArrayList<Stock>();

    private static final int threshold=3;

    public void addStock(int storeId, int productId, int stockQuantity) {   // προσθήκη αντικειμένου τύπου stock στην λίστα με όλα τα αποθέματα 
        stocks.add(new Stock(productId, storeId, stockQuantity, true));
    }

   
    public List<Stock> getStock(int productId, int storeId) {   //ελεγχος αποθεματος για συγκεκριμενο προιόν και κατάστημα 
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

    public void updateStock(int productId, int storeId, int newQuantity) {  // ενημερωση αποθέματος 
        List<Stock>currentStocks=getStock(productId, storeId);
        if (currentStocks!=null){
            for(Stock s : currentStocks){
                s.setStockQuantity(newQuantity);
            }
        }
    }

    
    public void reduceStockOnPurchase(int productId, int storeId, int quantity) { //μειώνει το απόθεμα
        List<Stock> stockList = getStock(productId, storeId);
        if (!stockList.isEmpty()) {
            // Παίρνουμε το πρώτο διαθέσιμο απόθεμα για το προϊόν και το κατάστημα
            Stock stock = stockList.get(0);
            if (stock.getStockQuantity() >= quantity) {
                stock.reduceStock(quantity);
            } 
            else {
                System.out.println("Ανεπαρκές απόθεμα για το product ID: " + productId);
            }
        } 
        else {
            System.out.println("Δεν βρέθηκε απόθεμα για το product ID: " + productId + " στο store ID: " + storeId);
        }
    }
    

    public void getLowStockProducts() {      //GET αποθέματα με stock κάτω από 3
       
        for (Stock s : stocks) {
            if (s.getStockQuantity() <= threshold) {
                System.out.println("Το προιόν με κωδικό: " + s.getProductId() + " στο κατάστημα: " + s.getStoreId() + "  εχει χαμηλό απόθεμα: " + s.getStockQuantity());
            }
        }
    }
    

    public ArrayList<Stock> searchProductInOtherStores(int productId, int excludedStoreId) {
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
    

    public String getStockAsJson(int productId) {  //θα επιστρέψει το απόθεμα ενός προϊόντος σε JSON μορφή, για όλα τα καταστήματα που το έχουν διαθέσιμο
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
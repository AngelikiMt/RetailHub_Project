import java.util.*;
import java.util.stream.Collectors;

public class StockService {
    private List<Stock> stocks = new ArrayList<>();
    private static final int threshold=3;

    public void addStock(Stock stock) {   // προσθήκη αντικειμένου τύπου stock στην λίστα με όλα τα αποθέματα 
        stocks.add(stock);
    }

    public List<Stock> getStock(int productId, int storeId) {     //ελεγχος αποθεματος για συγκεκριμενο προιόν και κατάστημα 
        if (stocks.size()>0){
           return stocks.filter(s -> s.getProductId() == productId && s.getStoreId() == storeId);
        }
        else{
            return null ;
        }
    }

    public void updateStock(int productId, int storeId, int newQuantity) {  // ενημερωση αποθέματος 
        ArrayList<Stock>currentStocks=getStock(productId, storeId);
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
                System.out.println("Insufficient stock for product ID: " + productId);
            }
        } 
        else {
            System.out.println("Stock not found for product ID: " + productId + " in store ID: " + storeId);
        }
    }
    

    public ArrayList<Stock> getLowStockProducts() {      //GET αποθέματα με stock κάτω από 3
        ArrayList<Stock> lowStockList = new ArrayList<>();
    
        for (Stock s : stocks) {
            if (s.getStockQuantity() < threshold) {
                lowStockList.add(s);
            }
        }
    
        return lowStockList;
    }
    

    public ArrayList<Stock> searchProductInOtherStores(int productId, int excludedStoreId) {
        ArrayList<Stock> result = new ArrayList<>();  // Νέα λίστα για τα αποτελέσματα
    
        for (Stock s : stocks) {  
            if (s.getProductId() == productId &&  // Αν το προϊόν ταιριάζει
                s.getStoreId() != excludedStoreId &&  // Αν δεν είναι το εξαιρούμενο κατάστημα
                s.isActiveProduct() &&  // Αν είναι ενεργό το προϊόν
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
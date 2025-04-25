import java.util.ArrayList;
import java.util.Iterator;

public class ProductService {
    private ArrayList<Product> products;

    public ProductService() {
        products = new ArrayList<>();
    }

    
    // Δημιουργία νέου προϊόντος με το Id να δημιουργείται αυτόματα
  public void createProduct(String description, String category, float price, float cost) 
{
    Product newProduct = new Product(description, category, price, cost);
    products.add(newProduct);
    System.out.println("Product added successfully with ID: " + newProduct.getProductId());
}

    
    // Διαγραφή προϊόντος βάσει productId
    public void deleteProduct(int productId) {
        Iterator<Product> iterator = products.iterator();
        boolean found = false;

        while (iterator.hasNext()) {
            Product product = iterator.next();
            if (product.getProductId() == productId) {
                iterator.remove();
                found = true;
                System.out.println("Product deleted successfully.");
                break;
            }
        }

        if (!found) {
            System.out.println("No product found with the specified ID.");
        }
    }

    // Ενημέρωση στοιχείων προϊόντος
public void updateProduct(int productId, String description, String category, float price, float cost) {
    for (Product product : products) {
        if (product.getProductId() == productId) {
            product.setDescription(description);
            product.setCategory(category);
            product.setPrice(price);
            product.setCost(cost);
            System.out.println("Product updated successfully.");
            return;
        }
    }
    System.out.println("No product found with the specified ID.");
}



    // Υπολογισμός κερδοφορίας ενός προϊόντος
    public float calculateProfit(int productId) {
        for (Product product : products) {
            if (product.getProductId() == productId) {
                float profit = product.getPrice() - profit.getCost();
                System.out.println("Profit of Product" + productId + ": " + profit);
                return profit;
            }
        }
        System.out.println("No product found with the specified ID.");
        return 0;
    }

    // Για εμφάνιση όλων των προϊόντων σε μορφή που μοιάζει με πίνακα
public void displayAllProducts() {
    if (products.isEmpty()) {
        System.out.println("No product found.");
        return;
    }

    System.out.printf("%-6s | %-20s | %-15s | %-10s | %-10s%n",
            "ID", "Description", "Category", "Price", "Cost");
    System.out.println("------------------------------------------------------------------");

    for (Product product : products) {
        System.out.printf("%-6d | %-20s | %-15s | %-10.2f | %-10.2f%n",
                product.getProductId(),
                product.getDescription(),
                product.getCategory(),
                product.getPrice(),
                product.getCost());
    }

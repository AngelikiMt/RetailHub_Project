import java.util.ArrayList;
import java.util.Iterator;



public class ProductService {
	private ArrayList<Product> products;
	public Object deleteProduct;

	public ProductService() {
		products = new ArrayList<>();
	}


	// Δημιουργία νέου προϊόντος με το Id να δημιουργείται αυτόματα
	public Product createProduct(String description, String category, double price, double cost) 
	{  
		//Validation στοιχείων
		if (description == null || description.trim().isEmpty()) {
			System.out.println("Description cannot be empty.");
			return null;
		}
		if (category == null || category.trim().isEmpty()) {
			System.out.println("Category cannot be empty.");
			return null;
		}
		if (price < 0) {
			System.out.println("Price cannot be negative.");
			return null;
		}
		if (cost < 0) {
			System.out.println("Cost cannot be negative.");
			return null;
		}
		//Αν η καταχώρηση είναι σωστή:
		Product newProduct = new Product(description, category, price, cost);
		products.add(newProduct);
		System.out.println("Product added successfully with ID: " + newProduct.getProductId());
		return newProduct;
	}



	// Διαγραφή προϊόντος βάσει productId
	public void deleteProduct(long productId) {
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
	public void updateProduct(long productId, String description, String category, double price, double cost)
	{ boolean found = false;

	      for (Product product : products) {
			if (product.getProductId() == productId) {

				// Validation στοιχείων
				if (description == null || description.trim().isEmpty()) {
					System.out.println("Description cannot be empty.");
					return;
				}
				if (category == null || category.trim().isEmpty()) {
					System.out.println("Category cannot be empty.");
					return;
				}
				if (price < 0) {
					System.out.println("Price cannot be negative.");
					return;
				}
				if (cost < 0) {
					System.out.println("Cost cannot be negative.");
					return;
				}


				//Αν η καταχώρηση είναι σωστή:

				product.setDescription(description);
				product.setCategory(category);
				product.setPrice(price);
				product.setCost(cost);

				System.out.println("Product updated successfully:"+ productId + " " + description + " " + category + " " + price + " " + cost);
				return;
			}
	      }
	      if (!found) {
				System.out.println("No product found with the specified ID.");
			
		}
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
	}

	

	// Αναζήτηση προϊόντος βάσει ID
	public Product findProductById(int productId) {
		for (Product product : products) {
			if (product.getProductId() == productId) {
				return product;
			}
		}
		return null;
	}
	
}

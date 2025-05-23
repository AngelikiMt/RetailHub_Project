import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProductService {
	private static ArrayList<Product> products =  new ArrayList<>();;
	public Object deleteProduct;

	//public ProductService() {
	//	products = new ArrayList<>();
	//}

	// Create a new product with the Id automatically generated
	public Product createProduct(String description, String category, double price, double cost) 
	{  
		// Data validation
		if (description == null || description.trim().isEmpty()) {
			System.out.println("Description cannot be empty.");
			return null;
		}
		if (category == null || category.trim().isEmpty()) {
			System.out.println("Category cannot be empty.");
			return null;
		}
		else if (!category.equals("clothing") && !category.equals("beauty") && !category.equals("electronis")){
			System.out.println("Not a valid category.");
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

		// If entry is correct:
		Product newProduct = new Product(description, category, price, cost);
		products.add(newProduct);
		System.out.println("Product added successfully with ID: " + newProduct.getProductId());
		System.out.println(newProduct);
		return newProduct;
	}

	public void deactivateProduct(long productId,boolean active) {
		for (Product product:products) {
			if (product.getProductId() == productId) {
				product.setActive(active);
				String status = active ? "activated" : "deactivated";
           		System.out.println("The product with ID " + productId + " is " + status + ".");
          		return;
			}
		}
		System.out.println("No product found with this ID.");
	}

	// Delete product based on productId
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

	// Update product details
	public void updateProduct(long productId, String newDescription, String newCategory, double newPrice, double newCost) {
		boolean found = false;
		List<String> errors = new ArrayList<>();

	    for (Product product : products) {
			if (product.getProductId() == productId) {

				// Data validation
				if (newDescription != null && !newDescription.trim().isEmpty()) {
					product.setDescription(newDescription);
				}
				if (newCategory != null && !newCategory.trim().isEmpty()) {
					product.setCategory(newCategory);
				}
				if (newPrice < 0) {
					System.out.println("Price cannot be negative.");
				} 
				if (newCost < 0) {
					System.out.println("Cost cannot be negative.");
				} 
				
				product.setPrice(newPrice);
				product.setCost(newCost);
				System.out.println("Product updated successfully:" + "\n" + product);// + productId + " " + description + " " + category + " " + price + " " + cost);
				return ;
			}
	    }
	    if (!found) {
			System.out.println("No product found with the specified ID.");
		}
	}

	// To display all products in a table-like format
	public void displayAllProducts() {
		if (products.isEmpty()) {
			System.out.println("No product found.");
			return;
		}

		System.out.printf("%-6s | %-20s | %-15s | %-10s |  %-10s | %-10s%n",
				"ID", "Description", "Category", "Price", "Cost", "Active");
		System.out.println("----------------------------------------------------------------------");

		for (Product product : products) {
			System.out.printf("%-6d | %-20s | %-15s | %-10.2f | %-10.2f | %-10b%n",
					product.getProductId(),
					product.getDescription(),
					product.getCategory(),
					product.getPrice(),
					product.getCost(),
					product.getActive());
		}
	}

	// Search product by ID
	public Product findProductById(long productId) {
		for (Product product : products) {
			if (product.getProductId() == productId) {
				return product;
			}
		}
		System.out.println("No product found with specific id.");
		return null;
	}
	
	// Returns product details in JSON format
	public static String getProductAsJson(Product product) {
		if (product == null) return "{}";

		StringBuilder json = new StringBuilder();
		json.append("{\n  \"productId\": ").append(product.getProductId()).append(",\n");
		json.append("  \"description\": ").append(product.getDescription()).append(",\n");
		json.append("  \"category\": ").append(product.getCategory()).append(",\n");
		json.append("  \"price\": ").append(product.getPrice()).append(",\n");
		json.append("  \"cost\": ").append(product.getCost()).append(",\n");
		json.append("  \"active\": ").append(product.getActive()).append("\n}");

		return json.toString();
	}
}

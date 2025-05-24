import java.util.ArrayList;
import java.util.List;

public class ProductService {
	private final static ProductDAO productDAO = new ProductDAO();

	// Create
	public static Product createProduct(String description, String category, Double price, Double cost) {
		List<String> errors = productValidation(description, category, price, cost);
		if (!errors.isEmpty()) {
			throw new IllegalArgumentException(String.join(", ", errors));
		}

		try {
			Product newProduct = new Product(description, category, price, cost);
			productDAO.createProduct(newProduct);
			return newProduct;	
		} catch (Exception e) {
			System.err.println("Failed to create product: " + e.getMessage()); // Log the error
            throw new RuntimeException("Failed to create product: " + e.getMessage(), e);
        }
	}

	// Update Product
    public static Product updateProduct(long productId, String description, String category, Double price, Double cost) {
        Product product = productDAO.getProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found.");
        }

        List<String> errors = productValidation(description, category, price, cost);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join(", ", errors));
        }

        product.setDescription(description);
        product.setCategory(category);
        product.setPrice(price);
        product.setCost(cost);

        productDAO.updateProduct(product);
        return product;
    }


    // Delete
	public boolean deleteProduct(long productId) {
		return productDAO.deleteProduct(productId);
	}

	// Activate/Deactivate
	public boolean setProductActiveStatus(long productId, boolean active) {
		return productDAO.setProductActive(productId, active);
	}

	// Show all products
	public List<Product> getAllProducts() {
		return productDAO.getAllProducts();
	}

	// Get a product by ID
	public Product findProductById(long productId) {
		return productDAO.getProductById(productId);
	}

	// Get stock as JSON string
    public static String getProductAsJson(long productId) {
        return productDAO.getProductAsJson(productId);
    }

	public static List<String> productValidation(String description, String category, Double price, Double cost) {
        List<String> errors = new ArrayList<>();

        if (description == null || description.trim().isEmpty()) {
            errors.add("Product description cannot be empty.");
        }
        if (category == null || category.trim().isEmpty()) {
            errors.add("Product category cannot be empty.");
        }
        if (!("clothing".equalsIgnoreCase(category) ||
              "beauty".equalsIgnoreCase(category) ||
              "electronics".equalsIgnoreCase(category))) {
            errors.add("'" + category + "' is not a valid category. Choose: clothing, beauty, or electronics.");
        }
        if (price == null || price < 0 || cost == null || cost < 0) {
            errors.add("Price and cost must be non-negative values."); 
        }
        if (price != null && cost != null && price < cost) {
            errors.add("Price cannot be less than cost.");
        }

        return errors;
    }
}
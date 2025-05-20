import java.util.List;

public class ProductService {
	private final ProductDAO productDAO;

	public ProductService() {
		productDAO = new ProductDAO();
	}

	// Create
	public boolean addProduct(Product product) {
		if (isValid(product)) {
			return productDAO.insertProduct(product);
		}
		return false;
	}

	// Update
	public boolean updateProduct(Product product) {
		if (isValid(product)) {
			return productDAO.updateProduct(product);
		}
		return false;
	}

	// Delete
	public boolean deleteProduct(long productId) {
		return productDAO.deleteProduct(productId);
	}

	// Activate/Deactivate
	public boolean setProductActiveStatus(long productId, boolean active) {
		return productDAO.setProductActive(productId, active);
	}

	// Retrieve
	public List<Product> getAllProducts() {
		return productDAO.getAllProducts();
	}

	public Product findProductById(long productId) {
		return productDAO.getProductById(productId);
	}

	// Basic validation
	private boolean isValid(Product p) {
		if (p.getDescription() == null || p.getDescription().trim().isEmpty()) return false;
		if (p.getCategory() == null || p.getCategory().trim().isEmpty()) return false;
		if (!(p.getCategory().equalsIgnoreCase("clothing")
				|| p.getCategory().equalsIgnoreCase("beauty")
				|| p.getCategory().equalsIgnoreCase("electronics"))) return false;
		if (p.getPrice() < 0 || p.getCost() < 0) return false;
		return true;
	}
}

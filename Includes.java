
/* Includes holds a row from the INCLUDES table.
 * That is, which product was sold, in which transaction and how much quantity was sold. */

public class Includes {
	private long transactionId; // In which transaction was the purchase made
	private long productId; // Which product was purchased
	private int soldQuantity; // How many pieces were sold

	// Constructor
	public Includes(long transactionId, long productId, int soldQuantity) {
		this.transactionId = transactionId;
		this.productId = productId;
		this.soldQuantity = soldQuantity;
	}
	
	// Get/Read values
	public long getTransactionId() {
		return transactionId;
	}
	
	public long getProductId() {
		return productId;
	}
	
	public int getSoldQuantity() {
		return soldQuantity;
	}
	
	
	// Set/Update values
	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}
	
	public void setProductId(long productId) {
		this.productId = productId;
	}
	
	public void setSoldQuantity(int soldQuantity) {
		this.soldQuantity = soldQuantity;
	}
	
	// This give a nicer and cleaner look to the prints
	@Override
	public String toString() {
		return "Includes: Transaction: " + transactionId + " | Product: " + productId + " | sold quantity: " + soldQuantity;
	}
}

// Which product was purchased, in which transaction and how many pieces were sold

public class Includes {
	private long transactionId;
	private long productId;
	private int soldQuantity;

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

	@Override
	public String toString() {
		return "Includes: Transaction: " + transactionId + " | Product: " + productId + " | sold quantity: " + soldQuantity;
	}
}

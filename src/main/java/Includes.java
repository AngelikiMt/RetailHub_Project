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
	
	// Get values
	public long getTransactionId() {
		return transactionId;
	}
	
	public long getProductId() {
		return productId;
	}
	
	public int getSoldQuantity() {
		return soldQuantity;
	}

	@Override
	public String toString() {
		return "TransactionId: " + transactionId + "\nProductId: " + productId + "\nSold quantity: " + soldQuantity;
	}
}

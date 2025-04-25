public class IncludesService {
	/* Creates and returns a new Includes object.
	 * Parameters: Transaction object, Product object and sold quantity.
	 * Connects Transaction and Product with Includes. */
	public Includes createIncludes(Transaction transaction, Product product, int soldQuantity) {
	    return new Includes(transaction.getTransactionId(), product.getProductId(), soldQuantity);
	}
	
}

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionService {

    private final static TransactionDAO transactionDAO = new TransactionDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final StockDAO stockDAO = new StockDAO();
    private final ClientDAO clientDAO = new ClientDAO();

    public boolean isEligibleForDiscount(Client client) {
        return client.getClientSumTotal() > 400;
    }

    public ShowTotalResult calculateTotal(
            List<Long> productIds, List<Integer> quantities,
            int storeId, long clientId) {

        if (productIds.size() != quantities.size()) {
            throw new IllegalArgumentException("Mismatched product and quantity list sizes.");
        }

        if (!Store.isActive()) {
            throw new IllegalStateException("Store is not active.");
        }

        Client client = clientDAO.getClientById(clientId);
        if (client == null) {
            throw new IllegalArgumentException("Client not found: " + clientId);
        }

        double sumTotal = 0;

        for (int i = 0; i < productIds.size(); i++) {
            long productId = productIds.get(i);
            int quantity = quantities.get(i);

            Stock stock = stockDAO.getStock((int) productId, storeId);
            if (stock == null || stock.getStockQuantity() < quantity) {
                throw new RuntimeException("Insufficient stock for product ID: " + productId);
            }

            Product product = productDAO.getProductById((int) productId);
            if (product == null || !product.getActive()) {
                throw new RuntimeException("Invalid or inactive product ID: " + productId);
            }

            sumTotal += product.getPrice() * quantity;
        }

        double discount = isEligibleForDiscount(client) ? sumTotal * 0.2 : 0;

        return new ShowTotalResult(clientId, sumTotal, discount);
    }

    public Transaction createTransaction(
            List<Long> productIds, List<Integer> quantities, int storeId,
            long clientId, String paymentMethod) {

        ShowTotalResult totalResult = calculateTotal(productIds, quantities, storeId, clientId);
        double finalTotal = totalResult.getSumTotal() - totalResult.getDiscount();

        // Update stock
        for (int i = 0; i < productIds.size(); i++) {
            stockDAO.reduceStock(storeId, productIds.get(i), quantities.get(i));
        }

        // Update client info
        Client client = clientDAO.getClientById(clientId);
        client.setClientSumTotal(client.getClientSumTotal() + finalTotal);
        client.setLastPurchaseDate(LocalDate.now());
        clientDAO.updateClient(client);

        // Create transaction
        Transaction transaction = new Transaction(
                clientId, storeId, finalTotal, totalResult.getDiscount(), paymentMethod);
        
        // Create includes
        List<Includes> includesList = new ArrayList<>();
        for (int i = 0; i < productIds.size(); i++) {
            includesList.add(new Includes(transaction.getTransactionId(), productIds.get(i), quantities.get(i)));
        }

        transactionDAO.insertTransaction(transaction, includesList);  // Sets transactionId

        return transaction;
    }

    public static List<Transaction> getAllTransactions() {
        return transactionDAO.getAllTransactions();
    }

    public static List<Includes> getAllIncludes() {
        return transactionDAO.getAllIncludes();
    }

    // Get stock as JSON string
    public static String getTransactionAsJson(long transactionId) {
        return transactionDAO.getTransactionAsJson(transactionId);
    }

    public static class ShowTotalResult {
        private final long clientId;
        private final double sumTotal;
        private final double discount;

        public ShowTotalResult(long clientId, double sumTotal, double discount) {
            this.clientId = clientId;
            this.sumTotal = sumTotal;
            this.discount = discount;
        }

        public long getClientId() {
            return clientId;
        }

        public double getSumTotal() {
            return sumTotal;
        }

        public double getDiscount() {
            return discount;
        }

        @Override
        public String toString() {
            return "Client ID: " + clientId + ", Sum Total: " + sumTotal + ", Discount: " + discount;
        }
    }
}

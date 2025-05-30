import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionService {

    private final static TransactionDAO transactionDAO = new TransactionDAO();
    private final static ProductDAO productDAO = new ProductDAO();
    private final static StockDAO stockDAO = new StockDAO();
    private final static ClientDAO clientDAO = new ClientDAO();
    private final static StoreDAO storeDAO = new StoreDAO();

    // public static boolean isEligibleForDiscount(Client client) {
    //     return client.getClientCurrentTotal() > 400;
    // }
    public static double calculateDiscount(double clientCurrentTotal, double sumTotal) {
    if (clientCurrentTotal + sumTotal >= 400) {
        return sumTotal * 0.20;
    }
    return 0;
    }


    public static ShowTotalResult calculateTotal(
            List<Long> productIds, List<Integer> quantities,
            int storeId, long clientId) {

        if (productIds.size() != quantities.size()) {
            throw new IllegalArgumentException("Mismatched product and quantity list sizes.");
        }

        Store store = storeDAO.getStoreById(storeId);
        if (store == null) {
            throw new IllegalArgumentException("Store not found: " + storeId);
        }
        if (!store.isActive()) {
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

            Stock stock = stockDAO.getStock(storeId, productId);
            if (stock == null || stock.getStockQuantity() < quantity) {
                throw new RuntimeException("Insufficient stock for product ID: " + productId);
            }

            Product product = productDAO.getProductById((int) productId);
            if (product == null || !product.getActive()) {
                throw new RuntimeException("Invalid or inactive product ID: " + productId);
            }

            sumTotal += product.getPrice() * quantity;
        }

        //double discount = isEligibleForDiscount(client) ? sumTotal * 0.2 : 0;
        double discount = calculateDiscount(client.getClientCurrentTotal(), sumTotal);
        
        return new ShowTotalResult(clientId, sumTotal, discount);
    }

    public static Transaction createTransaction(
            List<Long> productIds, List<Integer> quantities, int storeId,
            long clientId, String paymentMethod) {

        if (productIds == null || productIds.isEmpty() || quantities == null || quantities.isEmpty()) {
            throw new IllegalArgumentException("Product IDs and quantities cannot be empty for a transaction.");
        }
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
             throw new IllegalArgumentException("Payment method cannot be empty.");
        }
        
        ShowTotalResult totalResult = calculateTotal(productIds, quantities, storeId, clientId);
        double sumTotal = totalResult.getSumTotal();
        double discount = totalResult.getDiscount();
        double finalTotal = sumTotal - discount;
        
       // double finalTotal = totalResult.getSumTotal() - totalResult.getDiscount();


        // Update client info
        Client client = clientDAO.getClientById(clientId);
        double currentTotal = client.getClientCurrentTotal();

        if (currentTotal + sumTotal >= 400) {
            currentTotal = (currentTotal + sumTotal) - 400; // αφαιρείς 400 και κρατάς το υπόλοιπο
        } else {
            currentTotal += sumTotal;
        }

         // Update stock
        for (int i = 0; i < productIds.size(); i++) {
            stockDAO.reduceStock(storeId, productIds.get(i), quantities.get(i));
        }

        client.setClientSumTotal(client.getClientSumTotal() + finalTotal);
        client.setClientCurrentTotal(currentTotal); 
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

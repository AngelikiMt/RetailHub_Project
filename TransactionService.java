import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TransactionService {

    // Internal database
    private static final List<Transaction> transactions = new ArrayList<>();
    private static final List<Includes> includesList = new ArrayList<>();

    // Calculate Discount
    public static boolean calculateDiscount(Client client) {
        return client.getClientSumTotal() > 400;
    }

    // Show total
    public static ShowTotalResult showTotal(
            List<Long> productIds, List<Integer> quantities, int storeId,
            Client client, ProductService productService, StockService stockService) {

        if (productIds.size() != quantities.size()) {
            throw new IllegalArgumentException("Product and quantity lists size mismatch");
        }

        double sumTotal = 0;

        for (int i = 0; i < productIds.size(); i++) {
            long productId = productIds.get(i);
            int quantity = quantities.get(i);

            List<Stock> stockList = stockService.getStock((int) productId, storeId);
            if (stockList == null || stockList.isEmpty()) {
                throw new RuntimeException("No stock found for product ID: " + productId);
            }

            Stock stock = stockList.get(0);
            if (stock.getStockQuantity() < quantity) {
                throw new RuntimeException("Not enough stock for product ID: " + productId);
            }

            Product product = productService.findProductById((int) productId);
            if (product == null) {
                throw new RuntimeException("Product not found with ID: " + productId);
            }

            sumTotal += product.getPrice() * quantity;
        }

        double discount = calculateDiscount(client) ? sumTotal * 0.2 : 0;

        return new ShowTotalResult(client.getClientId(), sumTotal, discount);
    }

    // Create Transaction
    public static Transaction createTransaction(
            List<Long> productIds, List<Integer> quantities, int storeId,
            Client client, ProductService productService, StockService stockService, String paymentMethod) {

        ShowTotalResult totalResult = showTotal(productIds, quantities, storeId, client, productService, stockService);
        double finalTotal = totalResult.getSumTotal() - totalResult.getDiscount();

        // Update Stock
        for (int i = 0; i < productIds.size(); i++) {
            long productId = productIds.get(i);
            int quantity = quantities.get(i);
            stockService.reduceStockOnPurchase((int) productId, storeId, quantity);
        }

        // Update Client info
        client.setClientSumTotal(client.getClientSumTotal() + (float) finalTotal);
        client.setLastPurchaseDate(LocalDate.now());

        // Create new Transaction
        Transaction newTransaction = new Transaction(
                client.getClientId(),
                storeId,
                finalTotal,
                totalResult.getDiscount(),
                paymentMethod
        );
        transactions.add(newTransaction);

        // Create Includes records
        for (int i = 0; i < productIds.size(); i++) {
            long productId = productIds.get(i);
            int quantity = quantities.get(i);
            Includes include = new Includes(newTransaction.getTransactionId(), productId, quantity);
            includesList.add(include);
        }

        return newTransaction;
    }

    // Gets all transactions
    public static List<Transaction> getAllTransactions() {
        return transactions;
    }

    public static List<Includes> getAllIncludes() {
        return includesList;
    }

    // Result wrapper class
    public static class ShowTotalResult {
        private long clientId;
        private double sumTotal;
        private double discount;

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
            return "Client ID: " + clientId + " | Sum Total: " + sumTotal + " | Discount: " + discount;
        }
    }

    public static String getTransactionAsJson(Transaction transaction, List<Includes> includesList) {
        if (transaction == null) return "{}";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        StringBuilder json = new StringBuilder();

        json.append("{\n  \"transactionId\": ").append(transaction.getTransactionId()).append(",\n");
        json.append("  \"clientId\": ").append(transaction.getClientId()).append(",\n");
        json.append("  \"storeId\": ").append(transaction.getStoreId()).append(",\n");
        json.append("  \"dateTime\": \"").append(transaction.getDateTime().format(formatter)).append("\",\n");
        json.append("  \"paymentMethod\": \"").append(transaction.getPaymentMethod()).append("\",\n");
        json.append("  \"sumTotal\": ").append(transaction.getSumTotal()).append(",\n");
        json.append("  \"discount\": ").append(transaction.getDiscount()).append(",\n");

        // Includes 
        json.append("  \"includes\": [\n");
        List<Includes> transactionIncludes = includesList.stream().filter(i -> i.getTransactionId() == transaction.getTransactionId()).toList();

        for (int i = 0; i < transactionIncludes.size(); i++) {
            Includes inc = transactionIncludes.get(i);
            json.append("{\n  \"productId\": ").append(inc.getProductId()).append(",\n");
            json.append("  \"soldQuantity\": ").append(inc.getSoldQuantity()).append("\n}");

            if (i < transactionIncludes.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("  ]\n}");

        return json.toString();
    }
}

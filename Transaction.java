import java.time.LocalDateTime;

public class Transaction {

    private static long counter = 0;

    private long transactionId;
    private long clientId;
    private long storeId;
    private LocalDateTime dateTime;
    private String paymentMethod;
    private double sumTotal;
    private double discount;

    public Transaction(long clientId, long storeId, double sumTotal, double discount, String paymentMethod) {
        this.transactionId = ++counter;
        this.clientId = clientId;
        this.storeId = storeId;
        this.dateTime = LocalDateTime.now();
        this.paymentMethod = paymentMethod;
        this.sumTotal = sumTotal;
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "Transaction #" + transactionId +
                " | Client: " + clientId +
                " | Store: " + storeId +
                " | Total: " + sumTotal +
                " | Discount: " + discount +
                " | Payment: " + paymentMethod +
                " | DateTime: " + dateTime;
    }
}

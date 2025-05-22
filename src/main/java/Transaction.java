import java.time.LocalDateTime;

public class Transaction {
    private long transactionId;
    private long clientId;
    private int storeId;
    private LocalDateTime dateTime;
    private String paymentMethod;
    private double sumTotal;
    private double discount;

    public Transaction(long clientId, int storeId, double sumTotal, double discount, String paymentMethod) {
        this.clientId = clientId;
        this.storeId = storeId;
        this.dateTime = LocalDateTime.now();
        this.paymentMethod = paymentMethod;
        this.sumTotal = sumTotal;
        this.discount = discount;
    }

     // Constructor for loading transactions FROM THE DATABASE (ID and DateTime are already known)
    public Transaction(long transactionId, long clientId, int storeId, LocalDateTime dateTime,
                       String paymentMethod, double sumTotal, double doubleDiscount) {
        this.transactionId = transactionId;
        this.clientId = clientId;
        this.storeId = storeId;
        this.dateTime = dateTime;
        this.paymentMethod = paymentMethod;
        this.sumTotal = sumTotal;
        this.discount = doubleDiscount;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public long getClientId() {
        return clientId;
    }

    public int getStoreId() {
        return storeId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public double getSumTotal() {
        return sumTotal;
    }

    public double getDiscount() {
        return discount;
    }

    // Setter for transactionId, crucial for the DAO to set the generated ID
    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "\nTransaction ID: " + transactionId + "\nClientId: " + clientId + "\nStoreId: " + storeId +
                "\nTotal: " + sumTotal + "\nDiscount: " + discount + "\nPaymentMethod: " + paymentMethod + "\nDateTime: " + dateTime;
    }
}

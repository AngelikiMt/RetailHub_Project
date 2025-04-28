public class Stock {
    private long storeId;
    private long productId;
    private int stockQuantity;
    private boolean activeFlag;

    //Κατασκευαστής 
    public Stock(long storeId, long productId, int stockQuantity, boolean activeFlag) {
        this.storeId = storeId;
        this.productId = productId;
        this.stockQuantity = stockQuantity;
        this.activeFlag = activeFlag;
    }

    // Getters & Setters
    public long getStoreId() { return storeId; }
    public long getProductId() { return productId; }
    public int getStockQuantity() { return stockQuantity; }
    public boolean isActiveFlag() { return activeFlag; }

    public void setStockQuantity(int stockQuantity) {
        if (stockQuantity >= 0) {
          this.stockQuantity = stockQuantity;
        }
        else{
            System.out.println("Η ποσότητα δεν μπορεί να έχει αρνητική τιμή.");
        }
        
    }

    public void setActiveFlag(boolean activeFlag) {
        this.activeFlag = activeFlag;
    }

    public void reduceStock(int quantity) {
        if (quantity <= stockQuantity) {
            stockQuantity -= quantity;
        } 
        else {
            System.out.println("Error:Προσπαθείς να μειώσεις το απόθεμα σε αρνητικό αριθμό.");
        }
    }    

    @Override
    public String toString() {
        return "Stock{" +
                "storeId=" + storeId +
                ", productId=" + productId +
                ", stockQuantity=" + stockQuantity +
                ", activeFlag=" + activeFlag +
                '}';
    }
}

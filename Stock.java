public class Stock {
    private int storeId;
    private long productId;
    private int stockQuantity=0;
    private boolean activeFlag;

    // Constructor 
    public Stock(int storeId, long productId, int stockQuantity, boolean activeFlag) {
        this.storeId = storeId;
        this.productId = productId;
        this.stockQuantity = stockQuantity;
        this.activeFlag = activeFlag;
    }

    // Getters & Setters
    public int getStoreId() { return storeId; }
    public long getProductId() { return productId; }
    public int getStockQuantity() { return stockQuantity; }
    public boolean isActiveFlag() { return activeFlag; }

    public void setStockQuantity(int stockQuantity) {
        if (stockQuantity >= 0) {
          this.stockQuantity += stockQuantity;
        }
        else{
            System.out.println("The quantity cannot have a negative value.");
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
            System.out.println("Error: You are trying to reduce the inventory to a negative number.");
        }
    }    

    @Override
    public String toString() {
        return "Stock{" + "storeId=" + storeId + ", productId=" + productId +
                ", stockQuantity=" + stockQuantity + ", activeFlag=" + activeFlag + '}';
    }
}

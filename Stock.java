public class Stock {
    private int storeId;
    private int productId;
    private int stockQuantity;
    private boolean activeFlag;

    public Stock(int storeId, int productId, int stockQuantity, boolean activeFlag) {
        this.storeId = storeId;
        this.productId = productId;
        this.stockQuantity = stockQuantity;
        this.activeFlag = activeFlag;
    }

    public int getStoreId() { return storeId; }
    public int getProductId() { return productId; }
    public int getStockQuantity() { return stockQuantity; }
    public boolean isActiveFlag() { return activeFlag; }

    public void setStockQuantity(int stockQuantity) {
        if (stockQuantity > 0) {
          this.stockQuantity = stockQuantity;
        }
        else{
            System.out.println("Stock quantity cannot be negative.");
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
            System.out.println("Error: Attempt to reduce stock below zero.");
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

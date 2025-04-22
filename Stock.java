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

    public int getstoreId() { return storeId; }
    public int getproductId() { return productId; }
    public int getStockQuantity() { return stockQuantity; }
    public boolean isActiveFlag() { return activeFlag; }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void setActiveFlag(boolean activeFlag) {
        this.activeFlag = activeFlag;
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

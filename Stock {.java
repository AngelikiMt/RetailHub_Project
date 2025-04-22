public class Stock {
    private int idStore;
    private int idProduct;
    private int stockQuantity;
    private boolean activeFlag;

    public Stock(int idStore, int idProduct, int stockQuantity, boolean activeFlag) {
        this.idStore = idStore;
        this.idProduct = idProduct;
        this.stockQuantity = stockQuantity;
        this.activeFlag = activeFlag;
    }

    public int getIdStore() { return idStore; }
    public int getIdProduct() { return idProduct; }
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
                "idStore=" + idStore +
                ", idProduct=" + idProduct +
                ", stockQuantity=" + stockQuantity +
                ", activeFlag=" + activeFlag +
                '}';
    }
}

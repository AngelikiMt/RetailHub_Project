public class Product
{     
    private long productId;
    private String description;
    private String category;
    private double price;
    private double cost;
    private boolean active;


    // Constructor for creating NEW products (ID is NOT known yet)
    // The productId will be assigned by the DAO after DB insertion.
    public Product (String description, String category, double price, double cost) 
    {
        this.description = description;
        this.category = category;
        this.price = price;
        this.cost = cost;
        this.active = true; // Default to active for new products
    }

    // Constructor for loading products FROM THE DATABASE (ID is already known)
    // This is used by mapResultSetToProduct
    public Product(long productId, String description, String category, double price, double cost, boolean active) {
        this.productId = productId;
        this.description = description;
        this.category = category;
        this.price = price;
        this.cost = cost;
        this.active = active;
    }
    
    // Getters
    public long getProductId() {
        return productId;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public double getCost() {
        return cost;
    }

    public boolean getActive() {
        return active;
    }

    // Setters
    // Crucial for the DAO to set the generated ID after insertion.
    public void setProductId(long productId) {
        this.productId = productId;
    }

    public void setDescription(String description) {
        if (description != null) {
            this.description = description;
        } else {
            System.out.println("Description cannot be null.");
        }
    }

    public void setCategory(String category) 
    {
        if (category != null) {
            this.category = category;
        } else {
            System.out.println("Category cannot be null.");
        }
    }

    public void setPrice(double price) 
    {
        if (price >= 0) {
            this.price = price;
        } 
        else {
            System.out.println("The price cannot be negative.");
        }
    }
    public void setCost(double cost) {
        if (cost >= 0) {
            this.cost = cost;
        } else {
            System.out.println("The cost cannot be negative ");
        }
    }

    public void setActive (boolean active) {
        this.active = active;
    }

    @Override
    public String toString() 
    {
        return "Product ID: " + productId + "\n" + "Description: " + description + "\n" + 
        		"Category: " + category + "\n" + "Price: " + price + "\n" + "Cost: " + cost + "\n" +"Active: " + active;
    }
}

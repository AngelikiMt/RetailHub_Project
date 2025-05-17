public class Product
{ 
    private static long nextId = 1; // Auto-incremented internally 
    
    private long productId;
    private String description;
    private String category;
    private double price;
    private double cost;
    private boolean active;
//ffff

    // Constructor: productId to be automatically generated
    public Product (String description, String category, double price, double cost) 
    {
	    productId = nextId++;
        this.description = description;
        this.category = category;
        this.price = price;
        this.cost = cost;
        this.active = true;
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

    // Setters (without productId)
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

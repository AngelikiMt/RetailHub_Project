package product;

public class Product 
{ 
    private static long nextId = 1; //για να αυξάνεται το id αυτόματα
    
    private long productId;
    private String description;
    private String category;
    private double price;
    private double cost;


    // Constructor, με το productId να δημιουργείται αυτόματα
    public Product (String description, String category, double price, double cost) 
    {
	    this.productId = nextId++;
        this.description = description;
        this.category = category;
        this.price = price;
        this.cost = cost;
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

    
    //Setters (χωρίς productId)
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
    
    
 // Μέθοδος για να κάνουμε reset το nextId σε 1
	public static void resetId() 
	{
		 nextId = 0; // Επαναφορά του nextId σε 1
    }
		
    
	
    @Override
    public String toString() 
    {
        return "Product ID: " + productId + "\n" + 
               "Description: " + description + "\n" + 
        		"Category: " + category + "\n" + 
                "Price: " + price + "\n" + 
        		"Cost: " + cost;
    }
}

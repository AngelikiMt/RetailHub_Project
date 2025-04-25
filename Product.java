public class Product 
{ 
    private static int nextId = 1; //για να αυξανεται το id αυτόματα
    
    private int productId;
    private String description;
    private String category;
    private float price;
    private float cost;


// Constructor, με το productId να δημιουργείται αυτοματα
    public Product (String description, String category, float price, float cost) 
{
	this. productId = nextId++;
        this.description = description;
        this.category = category;
        this.price = price;
        this.cost = cost;
    }



    // Getters
public int getProductId() {
        return productId;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public float getPrice() {
        return price;
    }

    public float getCost() {
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
        this. category = category;
    } else {
        System.out.println("Category cannot be null.");
    }
}


    public void setPrice(float price) {
        if (price >= 0) 
{
            this.price = price;
        } 
else {
            System.out.println("The price cannot be negative.");
        }
    }
public void setCost(float cost) {
        if (cost >= 0) {
            this.cost = cost;
        } else {
            System.out.println("The cost cannot be negative ");
        }
    }



@Override
public String toString() 
{
    return "Product ID: " + productId + "\n"
         + "Description: " + description + "\n"
         + "Category: " + category + "\n"
         + "Price: " + price + "\n"
         + "Cost: " + cost;
}
}

public class Store {
	private int storeId; // Will be set by the DAO after DB insertion
	private String phone;
	private String address;
	private String country;
	private String storeName;
	private static boolean active;


	public Store (String storeName, String address, String country, String phone) {
		this.phone = phone;
		this.address = address;
		this.country = country;
		this.storeName = storeName;
		this.active = true;
	}

	// Constructor for loading stores FROM THE DATABASE (ID is already known)
    // This is used by mapResultSetToStore
    public Store(int storeId, String storeName, String address, String country, String phone, boolean active) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.address = address;
        this.country = country;
        this.phone = phone;
        this.active = active;
    }

	// Getters
	public int getStoreId() {
		return storeId;
	}

	public String getPhone() {
		return phone;
	}

	public String getAddress() {
		return address;
	}

	public String getCountry() {
		return country;
	}

	public String getStoreName() {
		return storeName;
	}

	// Is
	public static boolean isActive () {
		return active;
	}

 	// Setters
    // Crucial for the DAO to set the database-generated ID
    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

	public void setPhone (String phone) {
		this.phone=phone;
	}

	public void setAddress (String address) {
		this.address=address;
	}

	public void setCountry (String country) {
		this.country=country;
	}

	public void setStoreName (String storeName) {
		this.storeName=storeName;
	}
	
	public void setActive(boolean active) {
        this.active = active;
    }

	@Override
	public String toString(){
		return "Store ID: " + storeId + "\n" + "Store Name: " + storeName + "\n" + "Address: " + address + "\n"
			+ "Country: " + country + "\n" + "Phone: " + phone + "\n" + "Active: " + active + "\n";
	}
}





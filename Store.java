public class Store

{
	private static int nextId = 1;
	private int storeId;
	private String phone;
	private String address;
	private String country;
	private String storeName;
	//private int storeSum;
	private boolean active;


	public Store (String storeName, String address, String country, String phone)
	{
		storeId=nextId++;
		this.phone=phone;
		this.address=address;
		this.country=country;
		this.storeName=storeName;
		//storeSum = 0;
		active= true;
	}

	//getters
	public int getStoreId() {
		return storeId;
	}

	public String getphone() {
		return phone;
	}

	public String getaddress() {
		return address;
	}

	public String getcountry() {
		return country;
	}

	public String getstoreName() {
		return storeName;
	}

	//is
	public boolean isactive () {
		return active;
	}

 	//setters
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
		return "Store ID: " + storeId + "\n"
			+ "Store Name: " + storeName + "\n"
			+ "Address: " + address + "\n"
			+ "Country: " + country + "\n"
			+ "Phone: " + phone ;
	}
}





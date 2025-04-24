package retailhub;
	
public class store 

{
	private static int nextid = 1;
	private int storeid;  
	private int phone;
	private String address;
	private String country;
	private int storeSum;
	private String storeName;
	private boolean active;
	
	
	public store (int phone, String address, String country, String storeName)
	{
		storeid=nextid++;
		this.phone=phone;
		this.address=address;
		this.country=country;
		this.storeName=storeName;
		storeSum = 0;
		active= true;
	}
	
//getters
	public int getId() {
		return storeid;
	}
	
	public int getphone() {
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

	public boolean isactive () {
		return active;
	}
	
 //setters
	public void setphone (int phone) {
		this.phone=phone;
	}
	
	public void setaddress (String address) {
		this.address=address;
	}
}



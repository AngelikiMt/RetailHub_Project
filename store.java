package retailhub;
	
public class store 

{
	private static int nextid = 1;
	private int storeid;  
	private String phone;
	private String address;
	private String country;
	private String storeName;
	private int storeSum;
	private boolean active;
	
	
	public store (String phone, String address, String country, String storeName)
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
	public void setphone (String phone) {
		this.phone=phone;
	}
	
	public void setaddress (String address) {
		this.address=address;
	}

	public void setcountry (String country) {
		this.country=country;
	}

	public void storeName (String storeName) {
		this.storeName=storeName;
	}
	
}



package retailhub;

	
public class store 

{
	private static int nextid = 1;
	private int storeid;  
	private int phone;
	private String address;
	private String country;
	private int storeSum;
	private String storename;
	
	
	
	public store (int phone, String address, String country, String storename)
	{
		storeid=nextid++;
		this.phone=phone;
		this.address=address;
		this.country=country;
		this.storename=storename;
		storeSum = 0;
	}

	public int getId() {
		return storeid;
	}
	
	public int getphone() {
		return phone;
	}
	
	public void setphone (int phone) {
		this.phone=phone;
	}
	
	public String getaddress() {
		return address;
	}
	
	public void setaddress (String address) {
		this.address=address;
	}
	
	public String getcountry() {
		return country;
	}

	public String getstorename() {
		return storename;
	}
}



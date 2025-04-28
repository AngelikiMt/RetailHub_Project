import java.util.ArrayList;
import java.util.List;

public class storeService
{
    private ArrayList<Store> stores;

    
    public storeService() {
        stores = new ArrayList<>();
    } 

//validate inputs
    public void validate(String varname, String x, List<String> errors) {
        if (x == null || x.trim().isEmpty()) {
            errors.add( varname+ " cannot be empty.");
        }
    } 

  public void validatePhone(String phone, List<String> errors) {
        if (phone == null || phone.length() != 10 || !phone.matches("\\d{10}")) {
            errors.add("Phone number must be exactly 10 digits and contain only numbers.");
        }
    }

 //create store
 public Store createStore(String storeName, String address, String country, String phone) {
       List<String> errors = new ArrayList<>();
      
        validate("storeName",storeName, errors);
        validate("address",address, errors);
        validate("country",country, errors);
        validatePhone(phone, errors);

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("\n", errors));
        }
         else {
      storeName = storeName.trim();
      address = address.trim();
      country = country.trim();

      
      Store newStore= new Store(storeName, address, country, phone); 
      stores.add(newStore);     
      System.out.println("Store added successfully with ID:" + newStore.getstoreId());
      return newStore;
  }
 }

 //show store

 public void showStore(int storeId){
    for (Store store : stores) {
        if (store.getstoreId() == storeId) {
            System.out.println(store); 
        }
    }
 }

  //update store

  public void updateStore(int storeId, String newName, String newAddress, String newCountry, String newPhone) {  
    List<String> errors = new ArrayList<>();   
    for (Store store : stores) {
        if (store.getstoreId() == storeId) {

            if (newName != null) {
                validate("newName",newName, errors);
                if (errors.isEmpty()) store.setStoreName(newName);
            }
        
            if (newAddress != null) {
                validate("newAddress",newAddress, errors);
                if (errors.isEmpty()) store.setAddress(newAddress);
            }
        
            if (newPhone != null) {
                validatePhone(newPhone, errors);
                if (errors.isEmpty()) store.setPhone(newPhone);
            }
            
            if (!errors.isEmpty()) {
                throw new IllegalArgumentException(String.join("\n", errors));
            }
            System.out.println("Store with ID " + storeId+" updated successfully. The new info are");
            showStore(storeId);
                  
        }
        else{
            System.out.println("No store found with the specified ID.");
        }       
    }


}
}
    



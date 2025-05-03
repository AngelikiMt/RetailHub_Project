import java.util.ArrayList;
import java.util.List;

public class StoreService
{
    private static ArrayList<Store> stores= new ArrayList<>();

//SHOWS ALL STORES
    public static ArrayList<Store> getStores() {
        return stores ;
    }
//SHOW STORE INFO BY ID
    public static Store getStoreById(int storeId) {
        for (Store store : stores) {
            if (store.getStoreId() == storeId) {
                return store;
            }
        }
        return null; // Return null if not found
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

    public static boolean validateId(int storeId) {
        for (Store store : stores) {
            if (store.getStoreId() == storeId) {
                return true;
            }    
        }
        return false;
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
            System.out.println("Store added successfully with ID:" + newStore.getStoreId());
            return newStore;
  }
 }

  //update store
  public static void updateStore(int storeId, String newName, String newAddress, String newCountry, String newPhone) {  
        List<String> errors = new ArrayList<>();   
        for (Store store : stores) {
            if (store.getStoreId() == storeId) {

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
                break;    
            }
         }
     }
}
    



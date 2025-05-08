import java.util.ArrayList;
import java.util.List;

public class StoreService {
    private static ArrayList<Store> stores= new ArrayList<>();

    // Shows all stores
    public static ArrayList<Store> getStores() {
        return stores ;
    }

    // Shows store info by Id
    public static Store getStoreById(int storeId) {
        for (Store store : stores) {
            if (store.getStoreId() == storeId) {
                return store;
            }
        }
        return null; // Return null if not found
    }

    // Validates inputs
    public static void validate(String varname, String x, List<String> errors) {
        if (x == null || x.trim().isEmpty()) {
            errors.add( varname + " cannot be empty.");
        }
    } 

    public static void validatePhone(String phone, List<String> errors) {
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

    // Create store
    public static Store createStore(String storeName, String address, String country, String phone) {
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

    // Update store
    public static void updateStore(int storeId, String newName, String newAddress, String newCountry, String newPhone) {  
        List<String> errors = new ArrayList<>();   
        for (Store store : stores) {
            if (store.getStoreId() == storeId & store.isActive()) {

                if (!newName.isEmpty()) {
                    store.setStoreName(newName);
                }
            
                if (!newAddress.isEmpty()) {
                    store.setAddress(newAddress);
                }

                if (!newCountry.isEmpty()) {
                    store.setCountry(newCountry);
                }
            
                if (!newPhone.isEmpty()) {
                    validatePhone(newPhone, errors);
                    if (errors.isEmpty()) store.setPhone(newPhone);
                }
                
                if (!errors.isEmpty()) {
                    throw new IllegalArgumentException(String.join("\n", errors));
                }
                    
            }
        }
    }

    // Returns store details in JSON format
    public static String getStoreAsJson(Store store) {
        if (store == null) return "{}";

        StringBuilder json = new StringBuilder();
        json.append("{\n  \"storeId\": ").append(store.getStoreId()).append(",\n");
        json.append("  \"phoneNumber\": ").append(store.getPhone()).append(",\n");
        json.append("  \"address\": ").append(store.getAddress()).append(",\n");
        json.append("  \"country\": ").append(store.getCountry()).append(",\n");
        json.append("  \"storename\": ").append(store.getStoreName()).append(",\n");
        json.append("  \"active\": ").append(store.isActive()).append(",\n}");

        return json.toString();
    }
}
    



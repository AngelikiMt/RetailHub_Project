/** Acts as a service layer, providing business logic, 
 * validations for Store operations and sits between the UI (StoreFrame.java) 
 * and the Database layer (StoreDAO.java) */

import java.util.ArrayList;
import java.util.List;

public class StoreService {
    // For interacting with the database
    private final static StoreDAO storeDAO = new StoreDAO();
    private final StoreDAO storeDAO1;
    public StoreService() {
        this.storeDAO1 = new StoreDAO();
    }

    // Shows all stores
    public static List<Store> getStores() {
        return storeDAO.getAllStores();
    }

    // Shows store info by Id
    public static Store getStoreById(int storeId) {
        return storeDAO.getStoreById(storeId);
    }

    // Validating inputs
    // Checks if a string input is null or empty
    public static void validate(String varname, String x, List<String> errors) {
        if (x == null || x.trim().isEmpty()) {
            errors.add(varname + " cannot be empty.");
        }
    }

    // Checks if a phone number is exactly 10 digits and contains only numbers
    public static void validatePhone(String phone, List<String> errors) {
        if (phone == null || phone.length() != 10 || !phone.matches("\\d{10}")) {
            errors.add("Phone number must be exactly 10 digits and contain only numbers.");
        }
    }

    // Checks if a store ID exists in the DB by trying to retrieve it.
    public static boolean validateId(int storeId) {
        return storeDAO.getStoreById(storeId) != null;
    }

    // Creates store and returns a Store object
    public static Store createStore(String storeName, String address, String city, String country, String phone)
    {
        List<String> errors = new ArrayList<>();

        validate("storeName", storeName, errors);
        validate("address", address, errors);
        validate("city", city, errors);
        validate("country", country, errors);
        validatePhone(phone, errors);

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Validation errors:\n" + String.join("\n", errors));
        }
        try {
            Store newStore = new Store(storeName.trim(), address.trim(), city.trim(), country.trim(), phone.trim());
            if (storeDAO.createStore(newStore)) {
                System.out.println("Store added successfully with ID: " + newStore.getStoreId());
                return newStore;
            } else {
                throw new RuntimeException("Failed to create store: DAO returned false.");
            }
        } catch (RuntimeException e) { // Catch RuntimeException from DAO
            System.out.println("Error creating store: " + e.getMessage());
            throw e; // Re-throw to caller
        }
    }

    // Updates an existing store using its store ID.
    public static void updateStore(int storeId, String newName, String newAddress, String newCity, String newCountry, String newPhone) {
        Store store = storeDAO.getStoreById(storeId);

        if (store != null && store.isActive()) {
            if (!newName.isEmpty()) store.setStoreName(newName);
            if (!newAddress.isEmpty()) store.setAddress(newAddress);
            if (!newCity.isEmpty()) store.setCity(newCity);
            if (!newCountry.isEmpty()) store.setCountry(newCountry);
            if (!newPhone.isEmpty()) store.setPhone(newPhone);

        try {
                if (storeDAO.updateStore(store)) {
                    System.out.println("Store ID " + storeId + " updated successfully.");
                } else {
                    System.out.println("Failed to update store ID " + storeId + ".");
                }
            } catch (RuntimeException e) {
                System.out.println("Error updating store: " + e.getMessage());
            }
        } else {
            System.out.println("Store with ID " + storeId + " not found.");
        }
    }

    // Returns store details in JSON format
    public static String getStoreAsJson(int storeId) {
        return storeDAO.getStoreAsJson(storeId);
    }

    // Changes store's active status
    public boolean setStoreActiveStatus(int storeId, boolean activeStatus) {
        return storeDAO1.setStoreActive(storeId, activeStatus);
    }
}

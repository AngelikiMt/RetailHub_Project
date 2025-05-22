import java.util.ArrayList;
import java.util.List;

public class StoreService {

    private final static StoreDAO storeDAO = new StoreDAO();

    // Show all stores
    public static List<Store> getStores() {
        return storeDAO.getAllStores();
    }

    // Show store info by Id
    public static Store getStoreById(int storeId) {
        return storeDAO.getStoreById(storeId);
    }

    // Validate inputs
    public static void validate(String varname, String x, List<String> errors) {
        if (x == null || x.trim().isEmpty()) {
            errors.add(varname + " cannot be empty.");
        }
    }

    public static void validatePhone(String phone, List<String> errors) {
        if (phone == null || phone.length() != 10 || !phone.matches("\\d{10}")) {
            errors.add("Phone number must be exactly 10 digits and contain only numbers.");
        }
    }

    public static boolean validateId(int storeId) {
        return storeDAO.getStoreById(storeId) != null;
    }

    // Create store
    public static Store createStore(String storeName, String address, String country, String phone) throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
        List<String> errors = new ArrayList<>();

        validate("storeName", storeName, errors);
        validate("address", address, errors);
        validate("country", country, errors);
        validatePhone(phone, errors);

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Validation errors:\n" + String.join("\n", errors));
        }
        try {
            Store newStore = new Store(storeName.trim(), address.trim(), country.trim(), phone.trim());
            if (storeDAO.createStore(newStore)) {
                System.out.println("Store added successfully with ID: " + newStore.getStoreId());
                return newStore;
            } else {
                throw new RuntimeException("Failed to create store: DAO returned false.");
            }
        } catch (RuntimeException e) { // Catch RuntimeException from DAO
            System.err.println("Error creating store: " + e.getMessage());
            throw e; // Re-throw to caller
        }
    }

    // Update store
    public static void updateStore(int storeId, String newName, String newAddress, String newCountry, String newPhone) {
        Store store = storeDAO.getStoreById(storeId);

        if (store != null && store.isActive()) {
            if (!newName.isEmpty()) store.setStoreName(newName);
            if (!newAddress.isEmpty()) store.setAddress(newAddress);
            if (!newCountry.isEmpty()) store.setCountry(newCountry);
            if (!newPhone.isEmpty()) store.setPhone(newPhone);

        try {
                if (storeDAO.updateStore(store)) {
                    System.out.println("Store ID " + storeId + " updated successfully.");
                } else {
                    System.out.println("Failed to update store ID " + storeId + ".");
                }
            } catch (RuntimeException e) {
                System.err.println("Error updating store: " + e.getMessage());
            }
        } else {
            System.out.println("Store with ID " + storeId + " not found.");
        }
    }

    // Returns store details in JSON format
    public static String getStoreAsJson(int storeId) {
        return storeDAO.getStoreAsJson(storeId);
    }

    // Method to change active status
    public static boolean setStoreActiveStatus(int storeId, boolean activeStatus) {
        return storeDAO.setStoreActive(storeId, activeStatus);
    }
}

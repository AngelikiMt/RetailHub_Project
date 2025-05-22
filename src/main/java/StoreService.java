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
            throw new IllegalArgumentException(String.join("\n", errors));
        }

        Store newStore = new Store(storeName.trim(), address.trim(), country.trim(), phone.trim());
        storeDAO.createStore(newStore);
        System.out.println("Store added successfully with ID: " + newStore.getStoreId());
        return newStore;
    }

    // Update store
    public static void updateStore(int storeId, String newName, String newAddress, String newCountry, String newPhone) {
        Store store = storeDAO.getStoreById(storeId);

        if (store != null && store.isActive()) {
            if (!newName.isEmpty()) store.setStoreName(newName);
            if (!newAddress.isEmpty()) store.setAddress(newAddress);
            if (!newCountry.isEmpty()) store.setCountry(newCountry);
            if (!newPhone.isEmpty()) store.setPhone(newPhone);

            storeDAO.updateStore(store);
        } else {
            System.out.println("Store not found or inactive.");
        }
    }

    // Returns store details in JSON format
    public static String getStoreAsJson(long storeId) {
        return storeDAO.getStoreAsJson(storeId);
    }
}

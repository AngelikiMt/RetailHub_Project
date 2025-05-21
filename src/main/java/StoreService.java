import java.util.ArrayList;
import java.util.List;

public class StoreService {

    private final StoreDAO storeDAO = new StoreDAO();

    // Show all stores
    public List<Store> getStores() {
        return storeDAO.getAllStores();
    }

    // Show store info by Id
    public Store getStoreById(int storeId) {
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

    public boolean validateId(int storeId) {
        return storeDAO.getStoreById(storeId) != null;
    }

    // Create store
    public Store createStore(String storeName, String address, String country, String phone) {
        List<String> errors = new ArrayList<>();

        validate("storeName", storeName, errors);
        validate("address", address, errors);
        validate("country", country, errors);
        validatePhone(phone, errors);

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("\n", errors));
        }

        Store newStore = new Store(storeName.trim(), address.trim(), country.trim(), phone.trim());
        storeDAO.insertStore(newStore);
        System.out.println("Store added successfully with ID: " + newStore.getStoreId());
        return newStore;
    }

    // Update store
    public void updateStore(int storeId, String newName, String newAddress, String newCountry, String newPhone) {
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
    public static String getStoreAsJson(Store store) {
        if (store == null) return "{}";

        StringBuilder json = new StringBuilder();
        json.append("{\n  \"storeId\": ").append(store.getStoreId()).append(",\n");
        json.append("  \"phoneNumber\": \"").append(store.getPhone()).append("\",\n");
        json.append("  \"address\": \"").append(store.getAddress()).append("\",\n");
        json.append("  \"country\": \"").append(store.getCountry()).append("\",\n");
        json.append("  \"storename\": \"").append(store.getStoreName()).append("\",\n");
        json.append("  \"active\": ").append(store.isActive()).append("\n}");

        return json.toString();
    }
}

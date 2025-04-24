package retailhub;

public class storeService; {
//validate inputs
  public void validateName(String storeName, List<String> errors) {
        if (storeName == null || storeName.trim().isEmpty()) {
            errors.add("Name cannot be empty.");
        }
    }

  public void validateAddress(String address, List<String> errors) {
        if (address == null || address.trim().isEmpty()) {
            errors.add("Address cannot be empty.");
        }
    }

  public void validateCountry(String country, List<String> errors) {
        if (country == null || country.trim().isEmpty()) {
            errors.add("Country cannot be empty.");
        }
    }

  public void validatePhone(String phone, List<String> errors) {
        if (phone == null || phone.length() != 10 || !phone.matches("\\d{10}")) {
            errors.add("Phone number must be exactly 10 digits and contain only numbers.");
        }
    }

 //creta store
 public Store createStore(String name, String address, String phoneNumber) {
        List<String> errors = new ArrayList<>();

        validateName(name, errors);
        validateAddress(address, errors);
        validatePhoneNumber(phoneNumber, errors);

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("\n", errors));
        }

      storeName = storeName.trim();
      address = address.trim();
      country = country.trim();

      return new store(storeName, address, phone, country); 
  }

  //update store
  public Store updateStore(

  
}

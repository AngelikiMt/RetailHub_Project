import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClientService {
	private static int count = 0;

    private static void validateName(String name, List<String> errors, String fieldName) {
        if (name == null || name.trim().isEmpty()) {
            errors.add(fieldName + " cannot be empty.");
        } else if (!name.matches("^[A-Za-zΑ-Ωα-ωΆ-Ώά-ώ\\s'-]+$")) {
            errors.add(fieldName + " contains invalid characters.");
        } else if (name.length() < 3) {
            errors.add("Name must contain more than 3 characters.");
        }
    }

    private static void validateEmail(String email, List<String> errors, List <Client> clients) {
        if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
            errors.add("Invalid email address.");
        } else {
            for (Client client : clients) {
                if (email.equals(client.getEmail())) {
                errors.add("This email is already used.");
                }
            }
        } 
    }

    private static void validatePhoneNumber(String phoneNumber, List<String> errors, List <Client> clients) {
        if (phoneNumber == null || !phoneNumber.matches("^\\d{10}$")) {
            errors.add("The phone number must have exactly 10 digits.");
        }
        else {
            for (Client client : clients) {
                if (phoneNumber.equals(client.getPhoneNumber())) {
                errors.add("This phone number is already used.");
                }
            }
        } 
    }

    private static void validateGender(String gender, List<String> errors) {
    if (gender == null || 
        !(gender.equalsIgnoreCase("MALE") || 
          gender.equalsIgnoreCase("FEMALE") || 
          gender.equalsIgnoreCase("OTHER"))) {

        errors.add("Gender must be either MALE, FEMALE, or OTHER.");
        }
    }

    private static void validateAge(LocalDate birthDate, List<String> errors) {

        if (birthDate == null) {
            errors.add( "Birth date cannot be empty.");
         }
        else if (birthDate!=null && birthDate.isAfter(LocalDate.now())) {
            errors.add("Enter a valid date of birth.");
        }
        else {
            long age = ChronoUnit.YEARS.between(birthDate, LocalDate.now());
            if (age < 15) {
                throw new IllegalArgumentException("Client must be over 15 years old.");
            }
        }
    }

    // 1. Customer registration
    public static Client createClient(List <Client> clients, String firstName, String lastName, LocalDate birthDate,
                                      String phoneNumber, String email, String gender, boolean activeStatus) {

        List<String> errors = new ArrayList<>();

        validateName(firstName, errors, "Name");
        validateName(lastName, errors, "Surname");
        validateEmail(email, errors, clients);
        validatePhoneNumber(phoneNumber, errors, clients);
        validateGender(gender, errors);
                                    
        
        
            try {
                validateAge(birthDate, errors);
            } catch (IllegalArgumentException e) {
                errors.add(e.getMessage());
            }
        
                                                    
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("\n", errors));
        }
                                    
        return new Client(firstName, lastName, birthDate, phoneNumber, email, gender,activeStatus);
    }
        

    // 2. Customer identification by email or phone
    public static boolean authenticateClient(Client client, String input) {
        return client.getEmail().equals(input) || client.getPhoneNumber().equals(input);
    }

    // 2a. Customer identification by email or phone
    public static long authenticateClient1(List<Client> clients,String input) {
        for (Client client : clients) {
            if (client.getEmail().equals(input) || client.getPhoneNumber().equals(input)){
                System.out.println(input + " Successfully authenticated for client with ID: " + client.getClientId() + " and name: " + client.getFirstName() + " "+ client.getLastName());
                return client.getClientId();
            }
			
        } 
        System.out.println("Client with: "+ input + " does not exist.");
        return -1;
    }

    // 2β. Customer identification by email or phone
    public static Client authenticateClient( List<Client>clients,String input) {
        for ( Client client : clients) {
            if (client.getEmail().equals(input) || client.getPhoneNumber().equals(input)){
                return client;
            }
        }
        throw new IllegalArgumentException("Invalid input. No client found.");
    }

    // 3. Update customer details (with optional new prices)
    public static Client updateClient(List <Client> clients,Client client, String newFirstName, String newLastName, String newEmail, String newPhoneNumber) {
    
        List<String> errors = new ArrayList<>();

        if (newFirstName != null && !newFirstName.equals("")) {
            validateName(newFirstName, errors, "Name");
            if (errors.isEmpty()) {
                client.setFirstName(newFirstName);
            }
        }

        if (newLastName != null && !newLastName.equals("")) {
            validateName(newLastName, errors, "Surname");
            if (errors.isEmpty()) {
                client.setLastName(newLastName);
            }
        }

        if (newEmail != null && !newEmail.equals("")) {
            validateEmail(newEmail, errors,clients);
            if (errors.isEmpty()) {
                client.setEmail(newEmail);
            }
        }

        if (newPhoneNumber != null && !newPhoneNumber.equals("")) {
            validatePhoneNumber(newPhoneNumber, errors, clients);
            if (errors.isEmpty()) {
                client.setPhoneNumber(newPhoneNumber);
            }
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("\n", errors));
        }

        return client;
    }

    // 4. Turns customer's private info into null. The system keeps his purchases.
    public static void deleteClient(List<Client> clients, long clientId) {
        Iterator<Client> iterator = clients.iterator();

        while (iterator.hasNext()) {
            Client client = iterator.next();
            if (client.getClientId() == clientId) {
                client.setActiveStatus(false);
                client.setEmail(null);
                client.setBirthDate(null);
				client.setPhoneNumber(null);
                client.setFirstName(null);
                client.setLastName(null);
                break;
            }
        }
    }
	
    // 5. Checks if the customer has been inactive for more than 5 years. If true removes him. 
    public static void isInactiveMoreThan5Years(List<Client> clients) {
        Iterator<Client> iterator = clients.iterator();

        while (iterator.hasNext()) {
            Client client = iterator.next();
            long yearsInactive = ChronoUnit.YEARS.between(client.getLastPurchaseDate(), LocalDate.now());
            if (yearsInactive > 5) {
                iterator.remove(); // Remove client permanently
                count++;
            }
            System.out.println(count + " clients have been removed.");
        }
		count = 0;
    }

    // Returns client details in JSON format
    public static String getClientAsJson(Client client) {
        if (client == null) return "{}";

        StringBuilder json = new StringBuilder();
        json.append("{\n  \"clientId\": ").append(client.getClientId()).append(",\n");
        json.append("  \"firstName\": ").append(client.getFirstName()).append(",\n");
        json.append("  \"lastName\": ").append(client.getLastName()).append(",\n");
        json.append("  \"birthDate\": ").append(client.getBirthDate()).append(",\n");
        json.append("  \"phoneNumber\": ").append(client.getPhoneNumber()).append(",\n");
        json.append("  \"email\": ").append(client.getEmail()).append(",\n");
        json.append("  \"gender\": ").append(client.getGender()).append(",\n");
        json.append("  \"activeStatus\": ").append(client.isActiveStatus()).append(",\n");
        json.append("  \"dateJoined\": ").append(client.getDateJoined()).append(",\n");
        json.append("  \"clientSumTotal\": ").append(client.getClientSumTotal()).append(",\n");
        json.append("  \"lastPurchaseDate\": ").append(client.getLastPurchaseDate()).append("\n}");

        return json.toString();
    }
}

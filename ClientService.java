import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClientService {

    // 1. Εγγραφή πελάτη
    public static Client createClient( String firstName, String lastName, LocalDate birthDate,
                                      String phoneNumber, String email, String gender, boolean activeStatus,
                                      LocalDate dateJoined, double clientSumTotal, LocalDate lastPurchaseDate) {

    List<String> errors = new ArrayList<>();

    validateName(firstName, errors, "Name");
    validateName(lastName, errors, "Surname");
    validateEmail(email, errors);
    validatePhoneNumber(phoneNumber, errors);
                                
     if (birthDate == null || birthDate.isAfter(LocalDate.now())) {
     errors.add("The date of birth cannot be in the future.");
     }
                                
     if (dateJoined == null || dateJoined.isAfter(LocalDate.now())) {
     errors.add("The registration date cannot be in the future.");
     }
                                
    if (lastPurchaseDate != null && lastPurchaseDate.isAfter(LocalDate.now())) {
    errors.add("The last purchase date cannot be in the future.");
     }
                                
    if (clientSumTotal < 0) {
    errors.add("The total customer amount cannot be negative.");
     }
                                
    if (!errors.isEmpty()) {
    throw new IllegalArgumentException(String.join("\n", errors));
     }
                                
    return new Client(firstName, lastName, birthDate, phoneNumber, email, gender,
    activeStatus, dateJoined, clientSumTotal, lastPurchaseDate);
     }
        
    

    // 2. Ταυτοποίηση πελάτη με email ή τηλέφωνο
    public static boolean authenticateClient(Client client, String input) {
        return client.getEmail().equals(input) || client.getPhoneNumber().equals(input);
    }

    // 3. Ενημέρωση στοιχείων πελάτη (με προαιρετικά νέα τιμές)
    public static Client updateClient(Client client, String newFirstName, String newLastName, String newEmail, String newPhoneNumber) {
        List<String> errors = new ArrayList<>();

        if (newFirstName != null) {
            validateName(newFirstName, errors, "Name");
            if (errors.isEmpty()) {
                client.setFirstName(newFirstName);
            }
        }

        if (newLastName != null) {
            validateName(newLastName, errors, "Surname");
            if (errors.isEmpty()) {
                client.setLastname(newLastName);
            }
        }

        if (newEmail != null) {
            validateEmail(newEmail, errors);
            if (errors.isEmpty()) {
                client.setEmail(newEmail);
            }
        }

        if (newPhoneNumber != null) {
            validatePhoneNumber(newPhoneNumber, errors);
            if (errors.isEmpty()) {
                client.setPhoneNumber(newPhoneNumber);
            }
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("\n", errors));
        }

        return client;
    }

    private static void validateName(String name, List<String> errors, String fieldName) {
        if (name == null || name.trim().isEmpty()) {
            errors.add(fieldName + " cannot be empty.");
        } else if (!name.matches("^[A-Za-zΑ-Ωα-ωΆ-Ώά-ώ\\s'-]+$")) {
            errors.add(fieldName + " contains invalid characters.");
        }
    }

    private static void validateEmail(String email, List<String> errors) {
        if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
            errors.add("Invalid email address.");
        }
    }

    private static void validatePhoneNumber(String phoneNumber, List<String> errors) {
        if (phoneNumber == null || !phoneNumber.matches("^\\d{10}$")) {
            errors.add("The phone number must have exactly 10 digits.");
        }
    }

    // 4. Διαγραφή πελάτη από λίστα πελατών
    public static void deleteClient(List<Client> clients, int clientId) {
        Iterator<Client> iterator = clients.iterator();
        while (iterator.hasNext()) {
            Client client = iterator.next();
            if (client.getClientId() == clientId) {
                iterator.remove();
                break;
            }
        }
    }

    // 5. Έλεγχος αν ο πελάτης είναι ανενεργός πάνω από 5 χρόνια
    public static boolean isInactiveMoreThan5Years(Client client) {
        if (client.getLastPurchaseDate() == null) return true;
        return ChronoUnit.YEARS.between(client.getLastPurchaseDate(), LocalDate.now()) > 5;
    }
}

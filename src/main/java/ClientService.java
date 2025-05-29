import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ClientService {
    private static final ClientDAO clientDAO = new ClientDAO();
    //private static int count = 0;

    public static ClientDAO getClientDAO() { return clientDAO; }

    private static void validateName(String name, List<String> errors, String fieldName) {
        if (name == null || name.trim().isEmpty()) {
            errors.add(fieldName + " cannot be empty.");
        } else if (!name.matches("^[A-Za-zΑ-Ωα-ωΆ-Ώά-ώ\\s'-]+$")) {
            errors.add(fieldName + " contains invalid characters.");
        } else if (name.length() < 3) {
            errors.add("Name must contain more than 3 characters.");
        }
    }

    private static void validateEmail(String email, List<String> errors, List<Client> clients, long currentClientId) {
        if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
            errors.add("Invalid email address.");
        } else {
            for (Client client : clients) {
                if (email.equalsIgnoreCase(client.getEmail()) && client.getClientId() != currentClientId) {
                    errors.add("This email is already used.");
                    break;
                }
            }
        }
    }

    private static void validatePhoneNumber(String phoneNumber, List<String> errors, List<Client> clients) {
        if (phoneNumber == null || !phoneNumber.matches("^\\d{10}$")) {
            errors.add("The phone number must have exactly 10 digits.");
        } else {
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
            errors.add("Birth date cannot be empty.");
        } else if (birthDate.isAfter(LocalDate.now())) {
            errors.add("Enter a valid date of birth.");
        } else {
            long age = ChronoUnit.YEARS.between(birthDate, LocalDate.now());
            if (age < 15) {
                throw new IllegalArgumentException("Client must be over 15 years old.");
            }
        }
    }

    // 1. Customer registration
    public static Client createClient(String firstName, String lastName, LocalDate birthDate,
                                      String phoneNumber, String email, String gender, boolean activeStatus) {

        List<Client> clients = clientDAO.getAllClients(); //EINA KALO NA DHMIOURGEITAI MESA STO CREATE ?
        List<String> errors = new ArrayList<>();

        validateName(firstName, errors, "Name");
        validateName(lastName, errors, "Surname");
        validateEmail(email, errors, clients, -1); // -1 -> No current client yet
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

        Client newClient = new Client(firstName, lastName, birthDate, phoneNumber, email, gender, activeStatus);
        clientDAO.insertClient(newClient);
        return newClient;
    }

    // 2. Customer identification by email or phone
    public static boolean authenticateClient(Client client, String input) {
        return client.getEmail().equals(input) || client.getPhoneNumber().equals(input);
    }

    // 2a. Get client ID by input
    public static long authenticateClient1(String input) {
        Client client = clientDAO.getClientByEmailOrPhone(input);
        if (client != null) {
            System.out.println(input + " Successfully authenticated for client with ID: " +
                    client.getClientId() + " and name: " + client.getFirstName() + " " + client.getLastName());
            return client.getClientId();
        }
        System.out.println("Client with: " + input + " does not exist.");
        return -1;
    }

    // 2b. Get full client object by input
    public static Client authenticateClient(String input) {
        Client client = clientDAO.getClientByEmailOrPhone(input);
        if (client != null) {
            return client;
        }
        throw new IllegalArgumentException("Invalid input. No client found.");
    }

    // 3. Update client details
    public static Client updateClient(Client client, String newFirstName, String newLastName,
                                      String newEmail, String newPhoneNumber) {

        List<Client> clients = clientDAO.getAllClients();
        List<String> errors = new ArrayList<>();

        if (newFirstName != null && !newFirstName.isEmpty()) {
            validateName(newFirstName, errors, "Name");
            if (errors.isEmpty()) client.setFirstName(newFirstName);
        }

        if (newLastName != null && !newLastName.isEmpty()) {
            validateName(newLastName, errors, "Surname");
            if (errors.isEmpty()) client.setLastName(newLastName);
        }

        if (newEmail != null && !newEmail.isEmpty()) {
            validateEmail(newEmail, errors, clients, client.getClientId());
            if (errors.isEmpty()) client.setEmail(newEmail);
        }

        if (newPhoneNumber != null && !newPhoneNumber.isEmpty()) {
            validatePhoneNumber(newPhoneNumber, errors, clients);
            if (errors.isEmpty()) client.setPhoneNumber(newPhoneNumber);
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("\n", errors));
        }

        clientDAO.updateClient(client);
        return client;
    }

    // 4. Anonymize client (soft delete)
    public static void deleteClient(long clientId) {
        clientDAO.anonymizeClient(clientId);
    }

    // 5. Remove clients inactive for over 5 years
    public static void isInactiveMoreThan5Years() {
        int removed = clientDAO.deleteClientsInactiveMoreThan5Years();
        System.out.println(removed + " clients have been removed.");
    }

    // 6. Return client as JSON
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
        json.append("  \"clientCurrentTotal\": ").append(client.getClientCurrentTotal()).append(",\n");
        json.append("  \"lastPurchaseDate\": ").append(client.getLastPurchaseDate()).append("\n}");

        return json.toString();
    }
}

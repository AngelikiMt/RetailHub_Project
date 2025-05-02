import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Client> clientList = new ArrayList<>();

        // 1. Δημιουργία νέου πελάτη μέσω ClientService
        Client client = new Client(
              
                "Despoina",
                "Makridou",
                LocalDate.of(2001, 5, 7),
                "6941234567",
                "despoinamakr@gmail.com",
                "Female",
                true,
                LocalDate.of(2022, 1, 10),
                540.75f,
                LocalDate.of(2018, 12, 20) // για παράδειγμα >5 έτη πριν
        );
        clientList.add(client);

        // 2. Ταυτοποίηση πελάτη
        boolean isAuthenticated = ClientService.authenticateClient(client, "6941234567");
        System.out.println("Identification by phone: " + (isAuthenticated ? "Successful" : "Unsuccessful"));

        // 3. Ενημέρωση στοιχείων
        ClientService.updateClient(client,"maria","Papadopoulou","maria123@gmail.com", "6958429933" );

        // 4. Έλεγχος αν είναι ανενεργός πάνω από 5 χρόνια
        boolean isInactive = ClientService.isInactiveMoreThan5Years(client);
        System.out.println("Inactive for over 5 years: " + (isInactive ? "Yes" : "No"));

        // 5. Διαγραφή πελάτη
        ClientService.deleteClient(clientList, 1);
        System.out.println("Number of customers after deletion: " + clientList.size());

        // Ενημέρωση email και τηλεφώνου μόνο
        ClientService.updateClient(client, null, null, "new.email@gmail.com", "6900000000");

       // Ενημέρωση όλων
       ClientService.updateClient(client, "Maria", "Papadopoulou", "maria.pap@gmail.com", "6999999999");

      // Ενημέρωση μόνο του ονόματος
      ClientService.updateClient(client, "Maria", null, null, null);
    }

    }


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Client> clientList = new ArrayList<>();

        // 1. Δημιουργία νέου πελάτη μέσω ClientService
        Client client = ClientService.createClient(
                1,
                "Despoina",
                "Makridou",
                LocalDate.of(2001, 5, 7),
                "6941234567",
                "despoinamakr@gmail.com",
                "Γυναίκα",
                true,
                LocalDate.of(2022, 1, 10),
                540.75f,
                LocalDate.of(2018, 12, 20) // για παράδειγμα >5 έτη πριν
        );
        clientList.add(client);

        // 2. Ταυτοποίηση πελάτη
        boolean isAuthenticated = ClientService.authenticateClient(client, "6941234567");
        System.out.println("Ταυτοποίηση με τηλέφωνο: " + (isAuthenticated ? "Επιτυχής" : "Ανεπιτυχής"));

        // 3. Ενημέρωση στοιχείων
        ClientService.updateClient(client, "despoina.new@gmail.com", "6999999999");

        // 4. Έλεγχος αν είναι ανενεργός πάνω από 5 χρόνια
        boolean isInactive = ClientService.isInactiveMoreThan5Years(client);
        System.out.println("Ανενεργός πάνω από 5 χρόνια: " + (isInactive ? "Ναι" : "Όχι"));

        // 5. Διαγραφή πελάτη
        ClientService.deleteClient(clientList, 1);
        System.out.println("Πλήθος πελατών μετά τη διαγραφή: " + clientList.size());
    }
}

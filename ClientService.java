
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;

public class ClientService {
// 1. Εγγραφή πελάτη
    public static Client createClient(int id, String firstname, String lastname, LocalDate birthDate,
                                      String phoneNumber, String email, String gender, boolean activeStatus,
                                      LocalDate dateJoined, float clientSumTotal, LocalDate lastPurchaseDate) {
        return new Client(id, firstname, lastname, birthDate, phoneNumber, email, gender,
                          activeStatus, dateJoined, clientSumTotal, lastPurchaseDate);
    }

    // 2. Ταυτοποίηση πελάτη με email ή τηλέφωνο
    public static boolean authenticateClient(Client client, String input) {
        return client.getEmail().equals(input) || client.getPhoneNumber().equals(input);
    }

    // 3. Ενημέρωση στοιχείων πελάτη (για παράδειγμα αλλαγή email και τηλεφώνου)
    public static void updateClient(Client client, String newEmail, String newPhone) {
        client.setEmail(newEmail);
        client.setPhoneNumber(newPhone);
    }

    // 4. Διαγραφή πελάτη από λίστα πελατών
    public static void deleteClient(List<Client> clients, int clientId) {
        Iterator<Client> iterator = clients.iterator();
        while (iterator.hasNext()) {
            Client client = iterator.next();
            if (client.getIdClient() == clientId) {
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

    //public static double calculateTotalSpent(long clientId, List<Transaction> transactions) {
      //  double total = 0;
        //for (Transaction t : transactions) {
          //  if (t.getidClient() == idClient) {
            //    total += (t.getSumTotal() - t.getDiscount()); // Πραγματικό ποσό πληρωμής
            //}
        //}
        //return total;
    //}
}

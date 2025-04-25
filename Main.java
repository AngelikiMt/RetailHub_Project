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
        
        // Δημιουργία αντικειμένου κλάσης StockService
        StockService stockService = new StockService();
        
        // Δημιουργία νέου αποθέματος
        stockService.addStock(0001, 0001, 4);
        
        // Εμφάνιση αποθέματος για συγκεκριμένο προιόν και κατάστημα 
        System.out.println("Το απόθεμα είναι: " + stockService.getStock( 0001,0001).get(0).getStockQuantity());
        
        // Ενημέρωση ποσότητας αποθεμάτος 
        stockService.updateStock(0001,0001,10);
        System.out.println("Το νέο απόθεμα είναι: " + stockService.getStock( 0001,0001).get(0).getStockQuantity());
        
        // Ενημέρωση αποθέματος έπειτα από αγορά 
        stockService.reduceStockOnPurchase(0001, 0001, 7);
        System.out.println("Το απόθεμα είναι: " + stockService.getStock( 0001,0001).get(0).getStockQuantity());
        
        // Εμφάνιση προιόντων που έχουν χαμηλή ποσότητα αποθέματος ( <=3 )
        stockService.getLowStockProducts();
        
        // Εμφάνιση  διαθέσιμων προιόντων σε άλλα καταστήματα
        stockService.searchProductInOtherStores(0001, 0002);
    }
}

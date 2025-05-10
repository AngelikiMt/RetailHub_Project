import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

    public class TestData {
        private static final Scanner in = new Scanner(System.in);
    
        private static final List<Client> clients = new ArrayList<>();
        private static final List<Includes> includesList = new ArrayList<>();
        private static final List<Transaction> transactions = new ArrayList<>();
    
        public static final ProductService productService = new ProductService();
        public static final StockService stockService = new StockService();

        private static final String ADMIN_PIN = "12345";
    
     
       /*public static void setupInitialData() {
            // Προϊόντα για συναλλαγές
            productService.createProduct("red lipstic", "lipstic", 50, 20);      // ID 1
            productService.createProduct("blue mascara", "Mascara", 60, 25);     // ID 2
            productService.createProduct("Dior make-up", "make-up", 180, 90); // ID 3
    
            // Κατάστημα
            StoreService.createStore("nik", "plat 20", "greece", "2310999888");
    
            // Στοκ για συναλλαγές
            stockService.addStock(1, 1, 10);
            stockService.addStock(1, 2, 10);
            stockService.addStock(1, 3, 10);
    
            // Πελάτης για συναλλαγές
            Client testClient = new Client("Lena", "Kiriakou", LocalDate.of(1993, 4, 5),
                    "6901230001", "lena@mail.com", "Female", true,
                    LocalDate.now(), 100f, LocalDate.now());
            clients.add(testClient);
        }*/
    
        public static void runClientTests() {
            ClientService clientservice = new ClientService();
            boolean running = true;
            while (running) {
                System.out.println("\n========= Retail Hub Menu =========");
                System.out.println("1.CREATE CLIENT");
                System.out.println("2.AUTHENTICATE CLIENT");
                System.out.println("3.UPDATE CLIENT");
                System.out.println("4. DELETE CLIENT");
                System.out.println("5.Inactive/Active More Than 5 Years");
                System.out.println("6. Get Client As Json");
                System.out.println("0. Έξοδος");
                System.out.print("Επιλογή: ");
                String choice = in.nextLine();
               
            
                switch (choice) {
                        case "1": menuCreateClient(clientservice);
                        case "2": 
                        case "3": 
                        case "4": 
                        case "5": 
                        case "6": 
                        case "7": 
                        case "0": running = false; break;
                        default: System.out.println("Μη έγκυρη επιλογή.");
                }
            }
        }



        public static void menuCreateClient(ClientService clientservice){
            long clientId;
            String firstName;
            String lastName;
            LocalDate birthDate;
            String phoneNumber;
            String email;
            String gender;
            boolean activeStatus;
            LocalDate dateJoined;

            System.out.println("First Name: ");
            firstName= in.nextLine();
            System.out.println("Last Name: ");
            lastName= in.nextLine();
            System.out.println("Phone Number: ");
            phoneNumber= in.nextLine();
            System.out.println("Email: ");
            email= in.nextLine();

            try{
            Client client = clientservice.createClient(firstName, lastName, null, phoneNumber, email, null, true);

            System.out.println("Client was successfully created with ID: " + client.getClientId());
            }
            catch(Exception x ){
                System.out.println(x.getMessage());
            }

        }

        public static void
           /* Client client = new Client(
                    "Despoina",
                    "Makridou", 
                    LocalDate.of(2001, 5, 7),
                    "6941234567", 
                    "despoinamakr@gmail.com", 
                    "Female",
                    true, LocalDate.of(2022, 1, 10), 540.75f,
                    LocalDate.of(2018, 12, 20)
            );
            clients.add(client);
    
            boolean auth = ClientService.authenticateClient(client, "6941234567");
            System.out.println("Αυθεντικοποίηση: " + (auth ? "Επιτυχής" : "Ανεπιτυχής"));
    
            ClientService.updateClient(client, "maria", "Papadopoulou", "maria123@gmail.com", "6958429933");
    
            boolean inactive = ClientService.isInactiveMoreThan5Years(client);
            System.out.println("Ανενεργός >5 χρόνια: " + (inactive ? "Ναι" : "Όχι"));
    
            ClientService.deleteClient(clients, 1);
            System.out.println("Πελάτες μετά τη διαγραφή: " + clients.size());
        }*/
    
        public static void runProductTests() {
            Product p1 = productService.createProduct("red", "dress", 50, 20); // ID 4
            System.out.println(p1);
    
            Product p2 = productService.createProduct("red", "dress", 50, 20); // ID 5
            System.out.println(p2);
    
            productService.updateProduct(1, "green", "dress", 50, 20);
            productService.deleteProduct(1);
        }
    
        public static void createStore() {
            Store store = StoreService.createStore("nik", "plat 20", "greece", "2310999888");
            System.out.println("Κατάστημα:\n" + store);
        }
    
        public static void manageStore() {
            System.out.print("Εισάγετε PIN: ");
            String entered = in.nextLine();
            if (!entered.equals(ADMIN_PIN)) {
                System.out.println("Μη έγκυρο PIN.");
                return;
            }
            else{
                
                System.out.print("ID καταστήματος: ");
                int id = Integer.parseInt(in.nextLine());
                if (!StoreService.validateId(id)) {
                    System.out.println("Δεν βρέθηκε κατάστημα.");
                    return;
                }
        
                System.out.print("Όνομα: ");
                String name = in.nextLine();
                System.out.print("Διεύθυνση: ");
                String address = in.nextLine();
                System.out.print("Χώρα: ");
                String country = in.nextLine();
                System.out.print("Τηλέφωνο: ");
                String phone = in.nextLine();
        
                StoreService.updateStore(id, name, address, country, phone);
                System.out.println("Νέα στοιχεία:\n" + StoreService.getStoreById(id));
            }
        }
    
        public static void runStockTests() {
            stockService.addStock(1, 1, 4);
            stockService.addStock(1, 2, -2); // test αρνητικής εισαγωγής
    
            System.out.println("Stock: " + stockService.getStock(1, 1).get(0).getStockQuantity());
    
            stockService.updateStock(1, 1, 10);
            System.out.println("Ενημερωμένο stock: " + stockService.getStock(1, 1).get(0).getStockQuantity());
    
            stockService.reduceStockOnPurchase(1, 1, 11);
            System.out.println("Stock μετά την αγορά: " + stockService.getStock(1, 1).get(0).getStockQuantity());
    
            stockService.getLowStockProducts();
            stockService.searchProductInOtherStores(1, 2);
        }
    
        /*public static void runTransactionTests() {
            Client client = clients.get(0);
    
            // Συναλλαγή 1 – προϊόν 3, qty 1
            TransactionService.createTransaction(List.of(3L), List.of(1), 1, client,
                    productService, stockService, includesList, transactions, "Cash");
            System.out.println(transactions.get(transactions.size() - 1));
    
            // Συναλλαγή 2 – προϊόν 4, qty 2
            TransactionService.createTransaction(List.of(4L), List.of(2), 1, client,
                    productService, stockService, includesList, transactions, "Card");
            System.out.println(transactions.get(transactions.size() - 1));
    
            // Συναλλαγή 3 – προϊόν 5, qty 3 -> πάνω από 400€ (έκπτωση)
            TransactionService.createTransaction(List.of(5L), List.of(3), 1, client,
                    productService, stockService, includesList, transactions, "Credit");
            System.out.println(transactions.get(transactions.size() - 1));
        }*/
}

